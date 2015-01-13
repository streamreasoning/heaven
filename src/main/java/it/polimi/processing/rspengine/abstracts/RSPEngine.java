package it.polimi.processing.rspengine.abstracts;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.InputRDFStream;

/**
 * @author Riccardo
 * 
 */
public interface RSPEngine extends EventProcessor<InputRDFStream>, Startable<ExecutionState> {

	public ExecutionState startProcessing();

	public ExecutionState stopProcessing();

	public int getEventNumber();

	public String getName();

	public void timeProgress();

}
