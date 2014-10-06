package it.polimi.output.result;

import java.io.IOException;

import it.polimi.teststand.events.EventResult;
import it.polimi.teststand.events.ExperimentResult;


public interface ResultCollector {

	public boolean storeEventResult(EventResult r) throws IOException; 

	public boolean storeExperimentResult(ExperimentResult r);

	public long stop();

	public long start();

	public long getTimestamp();


}
