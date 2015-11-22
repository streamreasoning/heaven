package it.polimi.heaven.core.tsimpl.streamer.rdf2rdfstream;

import it.polimi.heaven.core.enums.ExecutionState;
import it.polimi.heaven.core.ts.EventProcessor;
import it.polimi.heaven.core.ts.events.Experiment;
import it.polimi.heaven.core.ts.events.Stimulus;
import it.polimi.heaven.core.ts.streamer.Streamer;
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

	protected final EventProcessor<Stimulus> next;

	protected ExecutionState status;
	protected int eventLimit;

}
