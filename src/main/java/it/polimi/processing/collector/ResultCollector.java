package it.polimi.processing.collector;

import it.polimi.processing.EventProcessor;

public interface ResultCollector<Event> extends EventProcessor<Event> {

	public boolean process(Event r, String where);

}
