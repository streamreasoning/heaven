package it.polimi.processing.rspengine.windowed.esper.commons.listener;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.windowed.esper.TripleEvent;

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

	private int eventNumber = 0;

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {

		log.debug("Run the Listener");

		Set<String[]> statements = new HashSet<String[]>();
		Set<String[]> ABoxTriples = new HashSet<String[]>();
		// TODO ottimizzazione duplicati lavorando su hashcode, struttura dati dedicata
		for (EventBean eventBean : newEvents) {
			System.out.println(eventBean.getUnderlying());
			TripleEvent storableEvent = (TripleEvent) eventBean.getUnderlying();
			for (String[] strings : storableEvent.getTriples()) {
				statements.add(strings);
				if (eventBean.get("channel").equals("Input")) {
					ABoxTriples.add(strings);
				}
			}
		}

		// try {
		// log.debug("Send Event to the StoreCollector");
		// // resultCollector.store(new Result(statements, eventNumber, (eventNumber +
		// // ABoxTriples.size()), System.currentTimeMillis()), "Result");
		// // resultCollector.store(new Result(ABoxTriples, eventNumber, (eventNumber +
		// // ABoxTriples.size()), System.currentTimeMillis()), "Window");
		// eventNumber += ABoxTriples.size() + 1;
		// } catch (IOException e) {
		// log.error("Something went wrong, can't save the event");
		// }
	}

}
