package it.polimi.processing.rspengine.esper.commons.listener;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.esper.TripleEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

@Data
@Log4j
@AllArgsConstructor()
@EqualsAndHashCode(callSuper = false)
public class ResultCollectorListener implements UpdateListener {

	private final ResultCollector<EventResult> resultCollector;
	private String name;
	private Set<String[]> statements;
	private Set<String[]> ABoxTriples;
	private int eventNumber = 0;

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {

		log.debug("Run the Listener");

		resetData();

		for (EventBean eventBean : newEvents) {
			TripleEvent storableEvent = (TripleEvent) eventBean.getUnderlying();
			statements.addAll(storableEvent.getTriples());
			if (eventBean.get("channel").equals("Input")) {
				ABoxTriples.addAll(storableEvent.getTriples());
			}
		}

		try {
			log.debug("Send Event to the StoreCollector");
			resultCollector.store(new Result(statements, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis()), "Result");
			resultCollector.store(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis()), "Window");
			eventNumber += ABoxTriples.size();
		} catch (IOException e) {
			log.error("Something went wrong, can't save the event");
		}
	}

	private void resetData() {
		statements = new HashSet<String[]>();
		ABoxTriples = new HashSet<String[]>();
	}
}
