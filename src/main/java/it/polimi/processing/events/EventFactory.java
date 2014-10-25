package it.polimi.processing.events;

import it.polimi.processing.events.Event;

import java.util.Set;

public interface EventFactory<T extends Event> {

	public T newEventInstance(Set<String[]> eventTriples, int lineNumber,
			String fileName);

	public T newEventInstance(String key, long ts, long tsevent, double memory,
			long latency, int lineNumber, String fileName);

}
