package it.polimi.processing.events.results;

import it.polimi.processing.events.interfaces.Event;

public interface EventResult extends Event {

	public boolean saveTrig(String where);

	public boolean saveCSV(String where);

}
