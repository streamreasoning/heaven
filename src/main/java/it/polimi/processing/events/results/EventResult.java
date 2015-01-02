package it.polimi.processing.events.results;

import it.polimi.processing.events.interfaces.Event;

public interface EventResult extends Event {

	public boolean save(String where);

}
