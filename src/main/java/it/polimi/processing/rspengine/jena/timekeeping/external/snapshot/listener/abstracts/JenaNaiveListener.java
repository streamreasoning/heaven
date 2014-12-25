package it.polimi.processing.rspengine.windowed.jena.timekeeping.external.snapshot.listener.abstracts;

import it.polimi.processing.ets.core.EventProcessor;
import it.polimi.processing.events.Result;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.windowed.jena.events.JenaEsperEvent;
import it.polimi.processing.system.ExecutionEnvirorment;
import it.polimi.processing.system.Memory;

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
			double ouputMemoryUsage = Memory.getMemoryUsage();

			if (next != null) {
				log.debug("Send Event to the StoreCollector");
				next.process(new Result(statements, eventNumber, (eventNumber + ABoxTriples.size()), outputTimestamp, ouputMemoryUsage, false));
				if (ExecutionEnvirorment.aboxLogEnabled) {
					next.process(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), outputTimestamp, ouputMemoryUsage, true));
				}
			}
			eventNumber += ABoxTriples.size() + 1;
		}
	}

	protected abstract Reasoner getReasoner();

}