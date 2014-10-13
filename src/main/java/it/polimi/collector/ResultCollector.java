package it.polimi.collector;

import it.polimi.enums.ExecutionStates;
import it.polimi.events.Event;

import java.io.IOException;
import java.util.Set;

public interface ResultCollector<T extends Event> {

	public boolean store(T r) throws IOException;

	public T newResultInstance(Set<String[]> all_triples, Event e);

	public ExecutionStates init();

	public ExecutionStates close();

	/**
	 * @return timestamp of the stop execution, 0 if an error happens
	 */
	public long getTimestamp();



}
