package it.polimi.processing.events;

import it.polimi.processing.events.interfaces.Event;

import java.util.Set;

public interface EventFactory<T extends Event> {

	public T newEventInstance(String key, Set<String[]> triple, int eventNumber, int experimentNumber);

}
