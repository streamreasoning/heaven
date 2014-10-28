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

import org.apache.log4j.Logger;

import com.espertech.esper.client.EventBean;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ResultCollectorListener extends GenearlListener {

	private final ResultCollector<StreamingEventResult> resultCollector;
	private final RSPEsperEngine engine;
	private Experiment experiment;

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {

		StreamingEventResult eventToSend;
		Set<String[]> statements = new HashSet<String[]>();
		Set<String[]> start_triples = new HashSet<String[]>();
		for (EventBean eventBean : newEvents) {
			Logger.getRootLogger().debug(eventBean.getUnderlying());

			TripleEvent storableEvent = (TripleEvent) eventBean.getUnderlying();

			statements.addAll(storableEvent.getTriples());

			if (eventBean.get("channel").equals("Input")) {
				start_triples.addAll(storableEvent.getTriples());
			}
		}

		eventToSend = resultCollector.newEventInstance(statements, engine.getCurrentStreamingEvent());

		try {
			Logger.getRootLogger().debug("SEND STORE EVENT");
			resultCollector.store(eventToSend, engine.getName() + "/" + experiment.getOutputFileName());
			Logger.getRootLogger().debug("SENT STORE EVENT");
		} catch (IOException e) {
			Logger.getRootLogger().debug("SEND NOt STORE EVENT");
		}
	}
}
