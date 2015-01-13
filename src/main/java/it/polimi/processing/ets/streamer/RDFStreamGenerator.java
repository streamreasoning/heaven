package it.polimi.processing.ets.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.InputRDFStream;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.profiler.abstracts.FlowRateProfiler;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.streamer.Parser;
import it.polimi.services.FileService;

import java.io.BufferedReader;
import java.io.IOException;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public final class RDFStreamGenerator extends TSStreamer {

	private InputRDFStream lastEvent;
	private String line;
	private int streamedEvents;
	private int triples;
	private Experiment currentExperiment;

	public RDFStreamGenerator(EventProcessor<InputRDFStream> processor, FlowRateProfiler<InputRDFStream> profiler, int eventLimit) {
		super(processor, profiler, ExecutionState.CLOSED, eventLimit);
	}

	public RDFStreamGenerator(EventProcessor<InputRDFStream> processor, FlowRateProfiler<InputRDFStream> profiler) {
		super(processor, profiler, ExecutionState.CLOSED, 1000);
	}

	@Override
	public boolean process(Experiment e) {
		if (!ExecutionState.READY.equals(status)) {
			throw new WrongStatusTransitionException("Can't Start in Status [" + status + "] must be [" + ExecutionState.READY + "]");
		} else {
			this.currentExperiment = e;
			try {
				log.info("Start Streaming");
				BufferedReader br = FileService.getBuffer(currentExperiment.getInputFileName());
				while ((line = br.readLine()) != null && streamedEvents <= eventLimit - 1) {

					status = ExecutionState.RUNNING;
					profiler.append(new TripleContainer(Parser.parseTriple(line)));
					triples++;

					if (profiler.isReady()) {

						streamedEvents += next.process(lastEvent = profiler.getEvent()) ? 1 : 0;

						log.debug("Send Event [" + streamedEvents + "] of size [" + lastEvent.size() + "]");
						log.debug("Streamed [" + triples + "] triples");

						if (streamedEvents % 1000 == 0) {
							log.info("Process Complete [" + (double) streamedEvents * 100 / eventLimit + "%]");
						}

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

	@Override
	public ExecutionState init() {
		status = ExecutionState.READY;
		streamedEvents = 0;
		triples = 0;
		return status;
	}

	@Override
	public ExecutionState close() {
		status = ExecutionState.CLOSED;
		return status;
	}

}