package it.polimi.processing.collector;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.workbench.core.Startable;

public interface StartableCollector<T extends Event> extends ResultCollector<T>, Startable<ExecutionState> {

}
