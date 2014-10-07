package it.polimi.output.result;

import it.polimi.events.Event;

import java.io.IOException;


public interface ResultCollector<T extends Event, E extends Event> {

	public boolean storeEventResult(T r) throws IOException; 

	public boolean storeExperimentResult(E r);

	public long stop();

	public long start();

	public long getTimestamp();


}
