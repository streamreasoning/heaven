package it.polimi;

import it.polimi.events.Event;

public interface  EventProcessor<T extends Event> {
	
	public abstract boolean sendEvent(T e);

}
