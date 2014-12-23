package it.polimi.processing.rspengine.windowed.jena.timecontrol.incremenal;

import it.polimi.processing.events.Result;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.windowed.jena.events.JenaEsperEvent;
import it.polimi.processing.system.ExecutionEnvirorment;
import it.polimi.processing.system.Memory;
import it.polimi.processing.workbench.core.EventProcessor;

import java.util.Arrays;
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

	protected final Model TBoxStar;
	protected Model abox;
	protected InfModel ABoxStar;

	protected Reasoner reasoner;
	protected final EventProcessor<Event> next;
	protected int eventNumber = 0;
	protected Set<TripleContainer> ABoxTriples;

	public JenaIncrementalListener(Model tbox, EventProcessor<Event> next) {
		this.TBoxStar = tbox;
		this.next = next;
		abox = ModelFactory.createMemModelMaker().createDefaultModel();
		ABoxTriples = new HashSet<TripleContainer>();
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {

		log.info("-- Event in Window [" + abox.size() + "] [" + ABoxStar.size() + "] [" + ABoxTriples.size() + "] --");
		if (oldData != null) {
			log.info("[" + newData.length + "] Old Events of type [" + newData[0].getUnderlying().getClass().getSimpleName() + "]");
			for (EventBean e : oldData) {
				log.info(e.getUnderlying().toString());
				JenaEsperEvent underlying = (JenaEsperEvent) e.getUnderlying();
				ABoxStar = ModelFactory.createInfModel((InfGraph) underlying.removeFrom(ABoxStar.getGraph()));
				ABoxTriples.removeAll(underlying.serialize());
			}
		}

		log.info("----");
		if (newData != null) {
			log.info("[" + newData.length + "] New Events of type [" + newData[0].getUnderlying().getClass().getSimpleName() + "]");
			for (EventBean e : newData) {
				log.info(e.getUnderlying().toString());
				JenaEsperEvent underlying = (JenaEsperEvent) e.getUnderlying();
				ABoxStar = ModelFactory.createInfModel((InfGraph) underlying.addTo(ABoxStar.getGraph()));
				ABoxTriples.addAll(underlying.serialize());
			}
		}

		Set<TripleContainer> statements = new HashSet<TripleContainer>();
		ABoxStar.rebind();
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
				for (TripleContainer tripleContainer : ABoxTriples) {
					System.err.println(Arrays.deepToString(tripleContainer.getTriple()));
				}
				next.process(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), outputTimestamp, ouputMemoryUsage, true));
			}
		}
		eventNumber += ABoxTriples.size() + 1;

	}

	protected abstract Reasoner getReasoner();

}