package it.polimi.streamer;

import it.polimi.events.Event;
import lombok.Getter;

@Getter
public abstract class EventProcessor<T extends Event> {
	
	protected String name;

	public abstract boolean sendEvent(T e);

}
