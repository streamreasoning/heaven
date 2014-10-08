package it.polimi.output.result;

import it.polimi.enums.ExecutionStates;
import it.polimi.events.Event;

import java.io.IOException;

public interface ResultCollector<T extends Event, E extends Event> {

	public boolean storeEventResult(T r) throws IOException;

	public boolean storeExperimentResult(E r);

	/**
	 * @return timestamp of the stop execution, 0 if an error happens
	 */
	public long getTimestamp();

	public ExecutionStates init();

	public ExecutionStates close();

}
