package it.polimi.processing.collector;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.results.EventResult;

public interface ResultCollector extends EventProcessor<EventResult>, Startable<ExecutionState> {

	public boolean process(EventResult r, String where);

}
