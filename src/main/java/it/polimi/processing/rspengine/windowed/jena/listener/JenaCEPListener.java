package it.polimi.processing.rspengine.windowed.jena.listener;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.Result;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.windowed.jena.events.JenaEsperEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.InfModelImpl;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.FileManager;

@Log4j
public abstract class JenaCEPListener implements UpdateListener {

	private final Model TBoxStar;
	private Graph abox;
	private InfModel ABoxStar;
	private Reasoner reasoner;
	private final ResultCollector<EventResult> collector;
	private int eventNumber = 0;
	private Set<TripleContainer> ABoxTriples;

	public JenaCEPListener(String tbox, ResultCollector<EventResult> collector) {
		FileManager.get().addLocatorClassLoader(this.getClass().getClassLoader());
		this.TBoxStar = FileManager.get().loadModel(tbox, null, "RDF/XML");
		this.collector = collector;
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {

		abox = ModelFactory.createMemModelMaker().createDefaultModel().getGraph();
		ABoxTriples = new HashSet<TripleContainer>();

		for (EventBean e : newData) {
			JenaEsperEvent underlying = (JenaEsperEvent) e.getUnderlying();
			ABoxTriples.addAll(underlying.serialize());
			abox = underlying.update(abox);
		}

		reasoner = getReasoner();
		InfGraph graph = reasoner.bindSchema(TBoxStar.getGraph()).bind(abox);
		ABoxStar = new InfModelImpl(graph);

		Set<TripleContainer> statements = new HashSet<TripleContainer>();
		StmtIterator iterator = ABoxStar.difference(TBoxStar).listStatements();

		Triple t;
		TripleContainer statementStrings;
		while (iterator.hasNext()) {
			t = iterator.next().asTriple();
			statementStrings = new TripleContainer(t.getSubject().toString(), t.getPredicate().toString(), t.getObject().toString());
			statements.add(statementStrings);
		}

		try {
			if (collector != null) {
				log.debug("Send Event to the StoreCollector");
				collector.store(new Result(statements, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis()), "Result");
				collector.store(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis()), "Window");
			}
			eventNumber += ABoxTriples.size() + 1;

		} catch (IOException e1) {
			log.error(e1.getMessage());
		}
	}

	protected abstract Reasoner getReasoner();

}