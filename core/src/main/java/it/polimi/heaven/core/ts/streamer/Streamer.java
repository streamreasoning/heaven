package it.polimi.heaven.core.ts.streamer;

import it.polimi.heaven.core.enums.ExecutionState;
import it.polimi.heaven.core.ts.EventProcessor;
import it.polimi.heaven.core.ts.Startable;

public interface Streamer<T> extends EventProcessor<T>, Startable<ExecutionState> {

}
