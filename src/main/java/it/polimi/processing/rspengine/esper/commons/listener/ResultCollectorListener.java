package it.polimi.processing.rspengine.esper.commons.listener;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.result.StreamingEventResult;
import it.polimi.processing.rspengine.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.esper.TripleEvent;

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

	private final ResultCollector<StreamingEventResult> resultCollector;
	private final RSPEsperEngine engine;
	private Experiment experiment;
	private Set<String[]> statements = new HashSet<String[]>();
	private Set<String[]> start_triples = new HashSet<String[]>();
	private StreamingEventResult eventToSend;

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {

		log.debug("Run the Listener");

		resetData();

		for (EventBean eventBean : newEvents) {
			TripleEvent storableEvent = (TripleEvent) eventBean.getUnderlying();
			statements.addAll(storableEvent.getTriples());

			if (eventBean.get("channel").equals("Input")) {
				start_triples.addAll(storableEvent.getTriples());
			}
		}

		eventToSend = resultCollector.newEventInstance(statements, engine.getCurrentStreamingEvent());

		try {
			log.debug("Send Event to the StoreCollector");
			resultCollector.store(eventToSend, engine.getName() + "/" + experiment.getOutputFileName());
		} catch (IOException e) {
			log.error("Somethign went wrong, can't save the event");
		}
	}

	private void resetData() {
		statements = new HashSet<String[]>();
		start_triples = new HashSet<String[]>();
		eventToSend = null;
	}
}
