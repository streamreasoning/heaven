package it.polimi.processing.collector;

import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.interfaces.Event;

import java.io.IOException;

public interface StartableCollector<T extends Event> extends ResultCollector<T>, Startable<ExecutionStates> {

	@Override
	public boolean store(T r) throws IOException;

	/**
	 * @return timestamp of the stop execution, 0 if an error happens
	 */

}
