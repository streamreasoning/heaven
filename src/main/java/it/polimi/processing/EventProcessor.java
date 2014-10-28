package it.polimi.processing;

import it.polimi.processing.events.Event;

public interface EventProcessor<T extends Event> {

	public abstract boolean sendEvent(T e);

}
