package it.polimi.processing.collector;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.workbench.core.Startable;

import java.io.IOException;

public interface StartableCollector<T extends Event> extends ResultCollector<T>, Startable<ExecutionState> {

	@Override
	public boolean store(T r) throws IOException;

	/**
	 * @return timestamp of the stop execution, 0 if an error happens
	 */

}
