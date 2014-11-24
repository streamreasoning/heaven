package it.polimi.processing.collector;

import it.polimi.processing.events.interfaces.Event;

import java.io.IOException;
import java.util.Set;

public interface ResultCollector<T extends Event> {

	public boolean store(T r) throws IOException;

	public T newEventInstance(Set<String[]> allTriples, Event e);

}
