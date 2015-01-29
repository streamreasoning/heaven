package it.polimi.processing.teststand.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.CTEvent;
import it.polimi.processing.events.profiler.abstracts.FlowRateProfiler;
import it.polimi.processing.streamer.Streamer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class TSStreamer implements Streamer<Experiment> {

	protected final EventProcessor<CTEvent> next;
	protected FlowRateProfiler<CTEvent> profiler;
	protected ExecutionState status;
	protected int eventLimit;

}
