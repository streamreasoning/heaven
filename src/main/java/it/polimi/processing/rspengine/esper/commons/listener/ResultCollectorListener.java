package it.polimi.processing.rspengine.esper.commons.listener;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.esper.TripleEvent;
import it.polimi.utils.Memory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.EventBean;

@Data
@Log4j
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ResultCollectorListener extends GenearlListener {

	private final ResultCollector<EventResult> resultCollector;
	private final RSPEsperEngine<RSPEvent> engine;
	private String name;
	private Set<String[]> statements;
	private Set<String[]> start_triples;
	private EventResult eventToSend;

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {

		log.debug("Run the Listener");

		resetData();

		for (EventBean eventBean : newEvents) {
			TripleEvent storableEvent = (TripleEvent) eventBean.getUnderlying();
			statements.addAll(storableEvent.getTriples());

			// if (eventBean.get("channel").equals("Input")) {
			// start_triples.addAll(storableEvent.getTriples());
			// }
		}

		RSPEvent eventToSend = engine.getCurrentStreamingEvent();
		eventToSend.setAll_triples(statements);
		eventToSend.setResultTimestamp(System.currentTimeMillis());
		eventToSend.setMemoryAR(Memory.getMemoryUsage());

		try {
			log.debug("Send Event to the StoreCollector");
			resultCollector.store(eventToSend);
		} catch (IOException e) {
			log.error("Something went wrong, can't save the event");
		}
	}

	private void resetData() {
		statements = new HashSet<String[]>();
		start_triples = new HashSet<String[]>();
		eventToSend = null;
	}
}
