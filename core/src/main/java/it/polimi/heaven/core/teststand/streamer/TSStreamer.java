package it.polimi.heaven.core.teststand.streamer;

import it.polimi.heaven.core.enums.ExecutionState;
import it.polimi.heaven.core.teststand.EventProcessor;
import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.rspengine.events.Stimulus;
import it.polimi.heaven.services.system.Memory;
import it.polimi.services.FileService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@NoArgsConstructor()
public final class TSStreamer extends Streamer {

	private String line;
	private int triples;
	private Stimulus[] last_stimuli;
	private HeavenInput last_input;
	private int streamedEvents;
	private ExecutionState status;
	@Setter
	private FlowRateProfiler profiler;

	public TSStreamer(int eventLimit, EventProcessor<Stimulus> engine, FlowRateProfiler profiler, ParsingTemplate parser) {
		super(eventLimit, engine, parser);
		this.profiler = profiler;
	}

	@Override
	public boolean start(FileReader in) {
		try {
			log.info("Start Streaming");
			BufferedReader br = FileService.getBuffer(in);
			while (streamedEvents <= eventLimit - 1) {
				line = br.readLine();
				status = ExecutionState.RUNNING;

				if (profiler.append(line)) {
					last_input = profiler.build();
					last_stimuli = last_input.getStimuli();
					last_input.setMemory_usage_before_processing(Memory.getMemoryUsage());
					collector.process(last_input);
					for (Stimulus stimulus : last_stimuli) {
						streamedEvents += engine.process(stimulus) ? 1 : 0;
					}
					log.debug("Streamed [" + triples + "] triples");
					log.info("Process Complete [" + (double) streamedEvents * 100 / eventLimit + "%]");
					// last_input.setEngine_size_inmemory(Memory.sizeOf(engine));
					// last_input.setTeststand_size_inmemory(Memory.sizeOf(collector));
					// last_input.setStreamer_size_inmemory(Memory.sizeOf(this));

					collector.process(last_input);
				} else {
					log.debug("Still Processing [" + line + "]");
				}
				status = ExecutionState.READY;
			}

			log.info("End Streaming: Triples: [" + triples + "] " + "RSPEvents: [" + streamedEvents + "]");
			br.close();
		} catch (IOException ex) {
			status = ExecutionState.ERROR;
			log.error(ex.getMessage());
		}
		return ExecutionState.READY.equals(status);
	}
}