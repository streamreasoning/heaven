package it.polimi.processing.collector;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionState;

public interface ResultCollector<Event> extends EventProcessor<Event>, Startable<ExecutionState> {

	public boolean process(Event r, String where);

}
