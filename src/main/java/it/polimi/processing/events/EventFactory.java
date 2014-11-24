package it.polimi.processing.events;

import it.polimi.processing.events.interfaces.Event;

public interface EventFactory<T extends Event> {

	public T newEventInstance(String id, String line, long comparationTimestamp);

}
