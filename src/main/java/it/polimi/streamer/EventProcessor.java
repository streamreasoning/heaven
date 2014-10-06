package it.polimi.streamer;

import it.polimi.teststand.events.Event;

public abstract class EventProcessor<T extends Event> {
	public abstract boolean sendEvent(T e);
}
