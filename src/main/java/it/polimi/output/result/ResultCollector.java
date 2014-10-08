package it.polimi.output.result;

import it.polimi.events.Event;
import it.polimi.teststand.enums.ExecutionStates;

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
