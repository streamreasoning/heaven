package it.polimi.processing.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.profiler.abstracts.FlowRateProfiler;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class RSPTripleSetStreamer implements Streamer<Experiment> {

	protected final EventProcessor<Event> next;
	protected FlowRateProfiler<RSPTripleSet> profiler;
	protected ExecutionState status;
	protected int eventLimit;

}
