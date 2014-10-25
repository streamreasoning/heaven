package it.polimi.processing.events;


public interface EventFactory<T extends Event> {

	public T newEventInstance(String key, long ts, long tsevent, double memory,
			long latency, int lineNumber, String fileName);

}
