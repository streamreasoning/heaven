package it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.listener.abstracts;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.results.Result;
import it.polimi.processing.rspengine.rspevents.jena.JenaEsperEvent;
import it.polimi.services.system.ExecutionEnvirorment;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.Reasoner;

@Log4j
public abstract class JenaIncrementalListener implements UpdateListener {

	protected Model abox;
	protected final Model TBoxStar;
	protected InfModel ABoxStar;

	protected Reasoner reasoner;
	protected final EventProcessor<Event> next;

	private int eventNumber = 0;
	private final Set<TripleContainer> ABoxTriples;
	private Set<TripleContainer> statements;

	public JenaIncrementalListener(Model tbox, EventProcessor<Event> next) {
		this.TBoxStar = tbox;
		this.next = next;
		abox = ModelFactory.createMemModelMaker().createDefaultModel();
		ABoxTriples = new HashSet<TripleContainer>();
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {

		log.debug("-- Event in Window [" + abox.size() + "] [" + ABoxStar.size() + "] [" + ABoxTriples.size() + "] --");
		if (oldData != null) {
			log.debug("[" + newData.length + "] Old Events of type [" + newData[0].getUnderlying().getClass().getSimpleName() + "]");
			for (EventBean e : oldData) {
				log.debug(e.getUnderlying().toString());
				JenaEsperEvent underlying = (JenaEsperEvent) e.getUnderlying();
				ABoxStar = ModelFactory.createInfModel((InfGraph) underlying.removeFrom(ABoxStar.getGraph()));
				ABoxTriples.removeAll(underlying.serialize());
			}
		}

		log.debug("----");
		if (newData != null) {
			log.debug("[" + newData.length + "] New Events of type [" + newData[0].getUnderlying().getClass().getSimpleName() + "]");
			for (EventBean e : newData) {
				log.debug(e.getUnderlying().toString());
				JenaEsperEvent underlying = (JenaEsperEvent) e.getUnderlying();
				ABoxStar = ModelFactory.createInfModel((InfGraph) underlying.addTo(ABoxStar.getGraph()));
				ABoxTriples.addAll(underlying.serialize());
			}
		}

		statements = new HashSet<TripleContainer>();
		ABoxStar.rebind(); // TODO verificare l'effort del rebind
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
			next.process(new Result("", statements, eventNumber, outputTimestamp, false));
			if (ExecutionEnvirorment.aboxLogEnabled) {
				next.process(new Result("", ABoxTriples, eventNumber, outputTimestamp, true));
			}
		}

	}

}