package it.polimi.rspengine.esper.commons.listener;

import it.polimi.collector.Collectable;
import it.polimi.collector.ResultCollector;
import it.polimi.events.result.StreamingEventResult;
import it.polimi.rspengine.esper.RSPEsperEngine;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import org.apache.log4j.Logger;

import com.espertech.esper.client.EventBean;

@Getter
public class ResultCollectorListener extends GenearlListener {

	private ResultCollector<StreamingEventResult> resultCollector;
	private RSPEsperEngine engine;

	public ResultCollectorListener(
			ResultCollector<StreamingEventResult> resultCollector,
			RSPEsperEngine e) {
		this.resultCollector = resultCollector;
		this.engine = e;
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {

		StreamingEventResult eventToSend;
		Set<String[]> statements = new HashSet<String[]>();
		Set<String[]> start_triples = new HashSet<String[]>();
		for (EventBean eventBean : newEvents) {
			Logger.getRootLogger().debug(eventBean.getUnderlying());

			Collectable storableEvent = (Collectable) eventBean.getUnderlying();

			statements.addAll(storableEvent.getTriples());

			if (eventBean.get("channel").equals("Input")) {
				start_triples.addAll(storableEvent.getTriples());
			}
		}

		eventToSend = resultCollector.newResultInstance(statements,
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
