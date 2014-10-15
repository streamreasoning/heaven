package it.polimi.processing.collector;

import it.polimi.processing.events.Event;

import java.io.IOException;
import java.util.Set;

public interface ResultCollector<T extends Event> {

	public boolean store(T r) throws IOException;

	public T newEventInstance(Set<String[]> all_triples, Event e);

	/**
	 * @return timestamp of the stop execution, 0 if an error happens
	 */
	public long getTimestamp();



}
