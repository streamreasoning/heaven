package it.polimi.heaven.core.ts.rspengine;

import it.polimi.heaven.core.ts.EventProcessor;
import it.polimi.heaven.core.ts.events.engine.Response;
import it.polimi.heaven.core.ts.events.heaven.HeavenEvent;
import it.polimi.heaven.core.ts.events.heaven.HeavenResult;
import it.polimi.heaven.services.system.Memory;
import lombok.extern.log4j.Log4j;

@Log4j
public class Receiver implements EventProcessor<Response> {

	private EventProcessor<HeavenEvent> collector;

	public Receiver() {
	}

	public Receiver(EventProcessor<HeavenEvent> collector) {
		this.collector = collector;
	}

	@Override
	public boolean process(Response response) {
		return collector.process(new HeavenResult(response, System.currentTimeMillis(), Memory.getMemoryUsage()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean setNext(EventProcessor<?> ep) {
		try {
			this.collector = (EventProcessor<HeavenEvent>) ep;
			return true;
		} catch (ClassCastException cce) {
			log.error(cce.getMessage());
			return false;
		}
	}
}
