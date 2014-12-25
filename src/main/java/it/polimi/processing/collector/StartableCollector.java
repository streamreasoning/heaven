package it.polimi.processing.collector;

import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.Event;

public interface StartableCollector<T extends Event> extends ResultCollector<T>, Startable<ExecutionState> {

}
