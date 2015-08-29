package it.polimi.heaven.core.ts.collector;

import it.polimi.heaven.core.enums.ExecutionState;
import it.polimi.heaven.core.ts.EventProcessor;
import it.polimi.heaven.core.ts.Startable;
import it.polimi.heaven.core.ts.events.EventResult;

public interface ResultCollector extends EventProcessor<EventResult>, Startable<ExecutionState> {

	public boolean process(EventResult r, String where);

}
