package it.polimi.heaven.core.ts.streamer.impl;

import it.polimi.heaven.core.enums.ExecutionState;
import it.polimi.heaven.core.ts.EventProcessor;
import it.polimi.heaven.core.ts.events.engine.Stimulus;
import it.polimi.heaven.core.ts.events.heaven.HeavenInput;
import it.polimi.heaven.core.ts.streamer.Encoder;
import it.polimi.heaven.core.ts.streamer.ParsingTemplate;
import it.polimi.heaven.core.ts.streamer.Streamer;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.FlowRateProfiler;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.TripleContainer;
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
public final class RDF2RDFStream extends Streamer {

	private String line;
	private int triples;
	private Stimulus[] last_stimuli;
	private HeavenInput last_input;
	private int streamedEvents;
	private ExecutionState status;
	@Setter
	private FlowRateProfiler<HeavenInput, TripleContainer> profiler;

	public RDF2RDFStream(ParsingTemplate parser) {
		super(parser);
	}

	public RDF2RDFStream(int eventLimit, Encoder encoder, EventProcessor<Stimulus> engine, FlowRateProfiler<HeavenInput, TripleContainer> profiler,
			ParsingTemplate parser) {
		super(eventLimit, encoder, engine, parser);
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
				profiler.append(new TripleContainer(parser.parse(line)));
				triples++;

				if (profiler.isReady()) {
					last_input = profiler.build();
					last_input.setEncoding_start_time(System.currentTimeMillis());
					last_stimuli = encoder.encode(last_input);
					last_input.setEncoding_end_time(System.currentTimeMillis());
					last_input.setStimuli(last_stimuli);
					for (Stimulus stimulus : last_stimuli) {
						streamedEvents += engine.process(stimulus) ? 1 : 0;
					}
					log.debug("Streamed [" + triples + "] triples");
					if (streamedEvents % 100 == 0) {
						log.info("Process Complete [" + (double) streamedEvents * 100 / eventLimit + "%]");
					}
					last_input.setMemory_usage_before_processing(Memory.getMemoryUsage());
					last_input.setEngine_size_inmemory(Memory.sizeOf(engine));
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