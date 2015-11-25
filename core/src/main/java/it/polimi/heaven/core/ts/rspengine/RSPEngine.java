package it.polimi.heaven.core.ts.rspengine;

import it.polimi.heaven.core.enums.ExecutionState;
import it.polimi.heaven.core.ts.EventProcessor;
import it.polimi.heaven.core.ts.Startable;
import it.polimi.heaven.core.ts.events.Stimulus;

/**
 * @author Riccardo
 * 
 */
public interface RSPEngine extends EventProcessor<Stimulus>, Startable<ExecutionState> {

	public ExecutionState startProcessing();

	public ExecutionState stopProcessing();

	public int getEventNumber();

	public String getName();

}
