package it.polimi.processing.events.interfaces;


public interface ExperimentResult extends Event {
	public boolean saveSQL(String where);
}
