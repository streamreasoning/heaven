package it.polimi.processing.events;

public interface EventFactory<T extends Event> {

	public T newEventInstance(String id, String[] lines, long timestamp,
			double memory, long latency, long comparationTimestamp);

}
