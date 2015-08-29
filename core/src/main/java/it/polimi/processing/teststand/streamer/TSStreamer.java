package it.polimi.processing.teststand.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.CTEvent;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.streamer.Streamer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * General implementation of the Streamer used by the TestStand
 * It process an Experiment event trough the method process(Experiment e).
 * It must be initialized before processing to stop the processing until next initialization.
 * It builds an flow rate according to the provided FlowRateProfiler and send events to the next
 * provided EventProcessor in CTEvent format
 * 
 * A limitation in the number of generated events can be set up in construction
 * 
 * 
 * 
 * @author Riccardo
 * 
 */
@Getter
@AllArgsConstructor
public abstract class TSStreamer implements Streamer<Experiment> {

	protected final EventProcessor<CTEvent> next;

	protected ExecutionState status;
	protected int eventLimit;

}
