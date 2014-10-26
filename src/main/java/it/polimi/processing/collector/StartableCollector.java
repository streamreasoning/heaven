package it.polimi.processing.collector;

import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.Event;

import java.io.IOException;

public interface StartableCollector<T extends Event> extends
		ResultCollector<T>, Startable<ExecutionStates> {

	public boolean store(T r, String where) throws IOException;

	/**
	 * @return timestamp of the stop execution, 0 if an error happens
	 */
	@Override
	public long getTimestamp();

}
