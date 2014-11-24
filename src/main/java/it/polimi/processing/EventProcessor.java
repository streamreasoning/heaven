package it.polimi.processing;

public interface EventProcessor<Event> {

	public abstract boolean sendEvent(Event streamingEvent);

}
