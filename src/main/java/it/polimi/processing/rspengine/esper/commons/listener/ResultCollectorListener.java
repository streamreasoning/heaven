package it.polimi.processing.rspengine.esper.commons.listener;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.result.StreamingEventResult;
import it.polimi.processing.rspengine.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.esper.TripleEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import org.apache.log4j.Logger;

import com.espertech.esper.client.EventBean;

@Getter
public class ResultCollectorListener extends GenearlListener {

	private final ResultCollector<StreamingEventResult> resultCollector;
	private final RSPEsperEngine engine;

	public ResultCollectorListener(
			ResultCollector<StreamingEventResult> resultCollector,
			RSPEsperEngine e) {
		this.resultCollector = resultCollector;
		this.engine = e;
	}

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

		eventToSend = resultCollector.newEventInstance(statements,
				engine.getCurrentStreamingEvent());

		try {
			Logger.getRootLogger().debug("SEND STORE EVENT");
			resultCollector.store(eventToSend);
			Logger.getRootLogger().debug("SENT STORE EVENT");
		} catch (IOException e) {
			Logger.getRootLogger().debug("SEND NOt STORE EVENT");
		}
	}
}
