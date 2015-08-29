package it.polimi.processing.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionState;

public interface Streamer<T> extends EventProcessor<T>, Startable<ExecutionState> {

}
