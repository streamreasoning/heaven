package it.polimi.processing.ets.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.factory.abstracts.FlowRateProfiler;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.streamer.Parser;
import it.polimi.processing.streamer.RSPTripleSetStreamer;

import java.io.BufferedReader;
import java.io.IOException;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public final class NTStreamer extends RSPTripleSetStreamer {

	private RSPTripleSet lastEvent;
	private String line;
	private int streamedEvents;
	private int triples;

	public NTStreamer(EventProcessor<Event> processor, FlowRateProfiler<RSPTripleSet> profiler, int eventLimit) {
		super(processor, profiler, ExecutionState.CLOSED, eventLimit);
	}

	public NTStreamer(EventProcessor<Event> processor, FlowRateProfiler<RSPTripleSet> profiler) {
		super(processor, profiler, ExecutionState.CLOSED, 1000);
	}

	@Override
	public void process(BufferedReader br, int experimentNumber) {
		try {
			if (!ExecutionState.READY.equals(status)) {
				throw new WrongStatusTransitionException("Can't Start in Status [" + status + "] must be [" + ExecutionState.READY + "]");
			} else {

				log.info("Start Streaming");

				while ((line = br.readLine()) != null && streamedEvents <= eventLimit - 1) {

					status = ExecutionState.RUNNING;
					profiler.append(new TripleContainer(Parser.parseTriple(line)));
					triples++;

					if (profiler.canSend()) {

						streamedEvents += next.process(lastEvent = profiler.getEvent()) ? 1 : 0;

						log.debug("Send Event [" + streamedEvents + "] triples [" + triples + "] of size [" + lastEvent.size() + "]");

						if (streamedEvents % 500 == 0) {
							log.info("Process Complete [" + (double) streamedEvents * 100 / eventLimit + "%]");
						}

					} else {
						log.debug("Still Processing [" + line + "]");
					}

					status = ExecutionState.READY;
				}

				log.info("End Streaming: Triples: [" + triples + "] " + "RSPEvents: [" + streamedEvents + "]");
				br.close();
			}
		} catch (IOException e) {
			status = ExecutionState.ERROR;
			log.error(e.getMessage());
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