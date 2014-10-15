package it.polimi.collector;

import it.polimi.Startable;
import it.polimi.enums.ExecutionStates;
import it.polimi.events.Event;

import java.io.IOException;

public interface StartableCollector<T extends Event> extends
		ResultCollector<T>, Startable<ExecutionStates> {

	public boolean store(T r) throws IOException;

	/**
	 * @return timestamp of the stop execution, 0 if an error happens
	 */
	public long getTimestamp();

}
