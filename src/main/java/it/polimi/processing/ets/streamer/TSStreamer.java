package it.polimi.processing.ets.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.InputRDFStream;
import it.polimi.processing.events.profiler.abstracts.FlowRateProfiler;
import it.polimi.processing.streamer.Streamer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class TSStreamer implements Streamer<Experiment> {

	protected final EventProcessor<InputRDFStream> next;
	protected FlowRateProfiler<InputRDFStream> profiler;
	protected ExecutionState status;
	protected int eventLimit;

}
