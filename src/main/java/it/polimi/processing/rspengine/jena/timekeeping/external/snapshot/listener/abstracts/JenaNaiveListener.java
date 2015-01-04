package it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.listener.abstracts;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.results.RSPTripleSetResult;
import it.polimi.processing.rspengine.rspevents.jena.JenaEsperEvent;
import it.polimi.services.system.ExecutionEnvirorment;

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
public abstract class JenaNaiveListener implements UpdateListener {

	private final Model TBoxStar;
	private Graph abox;
	private InfModel ABoxStar;
	private Reasoner reasoner;
	private final EventProcessor<Event> next;
	private int eventNumber = 0;
	private Set<TripleContainer> ABoxTriples;
	private int experimentNumber;

	public JenaNaiveListener(Model tbox, EventProcessor<Event> next) {
		this.TBoxStar = tbox;
		this.next = next;
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {

		if (oldData != null) {
			log.debug("[" + oldData.length + "] Old Events are still here");

		}

		if (newData != null) {

			log.debug("[" + newData.length + "] New Events of type [" + newData[0].getUnderlying().getClass().getSimpleName() + "]");

			abox = ModelFactory.createMemModelMaker().createDefaultModel().getGraph();
			ABoxTriples = new HashSet<TripleContainer>();

			for (EventBean e : newData) {
				log.debug(e.getUnderlying().toString());
				JenaEsperEvent underlying = (JenaEsperEvent) e.getUnderlying();
				abox = underlying.addTo(abox);
				ABoxTriples.addAll(underlying.serialize());
			}

			reasoner = getReasoner();
			InfGraph graph = reasoner.bindSchema(TBoxStar.getGraph()).bind(abox);
			ABoxStar = new InfModelImpl(graph);

			Set<TripleContainer> statements = new HashSet<TripleContainer>();
			Model difference = ABoxStar.difference(TBoxStar);
			StmtIterator iterator = difference.listStatements();

			Triple t;
			TripleContainer statementStrings;
			while (iterator.hasNext()) {
				t = iterator.next().asTriple();
				statementStrings = new TripleContainer(t.getSubject().toString(), t.getPredicate().toString(), t.getObject().toString());
				statements.add(statementStrings);
			}

			long outputTimestamp = System.currentTimeMillis();

			if (next != null) {
				log.debug("Send Event to the StoreCollector");
				eventNumber++;
				next.process(new RSPTripleSetResult("", statements, eventNumber, 0, outputTimestamp, false));
				if (ExecutionEnvirorment.aboxLogEnabled) {
					next.process(new RSPTripleSetResult("", ABoxTriples, eventNumber, 0, outputTimestamp, true));
				}
			}
		}
	}

	protected abstract Reasoner getReasoner();

}