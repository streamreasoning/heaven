package it.polimi.output.result;

import it.polimi.events.ExperimentEvent;
import it.polimi.events.ResultEvent;

import java.io.IOException;


public interface ResultCollector<T extends ResultEvent, E extends ExperimentEvent> {

	public boolean storeEventResult(T r) throws IOException; 

	public boolean storeExperimentResult(E r);

	public long stop();

	public long start();

	public long getTimestamp();


}
