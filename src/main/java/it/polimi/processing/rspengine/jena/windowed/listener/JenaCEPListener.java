package it.polimi.processing.rspengine.jena.windowed.listener;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.esper.commons.listener.Result;
import it.polimi.processing.rspengine.esper.plain.events.TEvent;
import it.polimi.utils.RDFSUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.InfModelImpl;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

@Log4j
public abstract class JenaCEPListener implements UpdateListener {

	private final Model TBoxStar;
	private Model abox;
	private InfModel ABoxStar;
	private Reasoner reasoner;
	private final ResultCollector<EventResult> collector;
	private int eventNumber = 0;
	private Set<String[]> ABoxTriples;

	public JenaCEPListener(String tbox, ResultCollector<EventResult> collector) {
		FileManager.get().addLocatorClassLoader(this.getClass().getClassLoader());
		this.TBoxStar = FileManager.get().loadModel(tbox, null, "RDF/XML");
		this.collector = collector;
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {

		abox = ModelFactory.createMemModelMaker().createDefaultModel();
		ABoxTriples = new HashSet<String[]>();

		for (EventBean e : newData) {
			TEvent underlying = (TEvent) e.getUnderlying();
			ABoxTriples.add(new String[] { underlying.getS(), underlying.getP(), underlying.getO() });
			abox.add(createStatement(underlying));
		}

		reasoner = getReasoner();
		InfGraph graph = reasoner.bindSchema(TBoxStar.getGraph()).bind(abox.getGraph());
		ABoxStar = new InfModelImpl(graph);

		Set<String[]> statements = new HashSet<String[]>();
		StmtIterator iterator = ABoxStar.difference(TBoxStar).listStatements();

		Triple t;
		String[] statementStrings;
		while (iterator.hasNext()) {
			t = iterator.next().asTriple();
			statementStrings = new String[] { t.getSubject().toString(), t.getPredicate().toString(), t.getObject().toString() };
			statements.add(statementStrings);
		}

		try {

			log.debug("Send Event to the StoreCollector");
			collector.store(new Result(statements, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis()), "Result");
			collector.store(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis()), "Window");
			eventNumber += ABoxTriples.size();

		} catch (IOException e1) {
			log.error(e1.getMessage());
		}
	}

	private Statement createStatement(TEvent e) {
		Resource subject = ResourceFactory.createResource(e.getS());
		Property predicate = (e.getP() != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(e.getP()) : RDF.type;
		RDFNode object = ResourceFactory.createResource(e.getO());
		return ResourceFactory.createStatement(subject, predicate, object);
	}

	protected abstract Reasoner getReasoner();

}