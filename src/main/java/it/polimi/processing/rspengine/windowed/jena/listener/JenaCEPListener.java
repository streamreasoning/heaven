package it.polimi.processing.rspengine.windowed.jena.listener;

import it.polimi.processing.events.Result;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.windowed.jena.events.JenaEsperEvent;
import it.polimi.processing.workbench.core.EventProcessor;

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

@Log4j
public abstract class JenaCEPListener implements UpdateListener {

	private final Model TBoxStar;
	private Graph abox;
	private InfModel ABoxStar;
	private Reasoner reasoner;
	private final EventProcessor<Event> next;
	private int eventNumber = 0;
	private Set<TripleContainer> ABoxTriples;

	public JenaCEPListener(Model tbox, EventProcessor<Event> next) {
		this.TBoxStar = tbox;
		this.next = next;
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {

		if (oldData != null) {
			log.debug("[" + oldData.length + "] Old Events are still here");
		}

		if (newData != null) {
			log.d("[" + newData.length + "] New Events");

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
			if (next != null) {
				log.debug("Send Event to the StoreCollector");
				// TODO add memory
				next.process(new Result(statements, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis(), false));
				next.process(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis(), true));
			}
			eventNumber += ABoxTriples.size() + 1;
		}
	}

	protected abstract Reasoner getReasoner();

}