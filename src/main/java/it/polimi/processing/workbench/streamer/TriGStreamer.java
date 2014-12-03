package it.polimi.processing.workbench.streamer;

import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.streamer.Parser;
import it.polimi.processing.streamer.RSPEventStreamer;
import it.polimi.processing.workbench.core.EventProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

/**
 * @author Riccardo
 * 
 * @param <T>
 *            The kind of class that will received the streamed events in this
 *            implementation it extends EventProcessor which offers sendEvent(E
 *            e) method. This class is parametric too, and the kind of event
 *            processed at this time is StreamingEvent
 */

@Getter
@Log4j
public class TriGStreamer extends RSPEventStreamer {

	/**
	 * Represents the core of the streaming procedure, is must publish the
	 * sendEvent method through which is possibile to inject any kind of event
	 * into the system
	 */
	private String line;
	private TriG eventTrig = null;
	private String currentTriG = "";
	private RSPEvent streamingEvent;

	public TriGStreamer(EventProcessor<RSPEvent> processor) {
		super(processor, ExecutionState.CLOSED);
	}

	/**
	 * Open the buffer reader that encapsulate a csv form input file
	 * 
	 * 
	 * @param status
	 *            represent the execution state of the Environment outside the
	 *            Streamer. Is exploit the dependency injection paradigm since
	 *            the streamer must be controlled by the outstanding application
	 *            system
	 **/
	@Override
	public void stream(BufferedReader br, int experimentNumber) throws IOException {

		if (!ExecutionState.READY.equals(status)) {
			throw new WrongStatusTransitionException("Not Ready " + status);
		} else {
			int eventNumber = 0;
			int graphSize = 0;
			while ((line = br.readLine()) != null && !line.isEmpty()) {
				graphSize++;
				if (currentTriG.equals(""))
					// Status One
					currentTriG += line;
				else {
					// Status Two
					currentTriG += Parser.EOF + line;
				}

				if (!"}".equals(line)) {
					continue;
					// Control the good format of the received TriG vefore sending
				} else if (Parser.triGMatch(currentTriG)) {
					// Status Tree

					eventTrig = Parser.parseTrigGraph(currentTriG);
					status = ExecutionState.RUNNING;

					if (sendEvent(eventTrig, eventNumber, experimentNumber)) {
						log.debug("Send [ " + line + " ] as a New StreamEvent");
						status = ExecutionState.READY;
						eventNumber++;
						resetCurrentTrig();

					} else {
						status = ExecutionState.READY;
						log.debug("Not Saved " + line);

					}

					log.debug("Total Graph Size " + graphSize);
					graphSize = 0;

					if (eventNumber % 1000 == 0) {
						log.info("STREAMED " + eventNumber + "EVENTS");
					}

				}

			}
			br.close();

		}
	}

	private void resetCurrentTrig() {
		currentTriG = "";
	}

	private boolean sendEvent(TriG trig, int eventNumber, int experimentNumber) {

		String key = trig.getKey();
		Set<TripleContainer> triples = trig.getTriples();

		if (trig != null && key != null && !key.isEmpty() && triples != null && triples.size() > 0) {
			streamingEvent = createEvent(key, new HashSet<TripleContainer>(triples), eventNumber, experimentNumber);
			return processor.process(streamingEvent);
		}
		return false;
	}

	@Override
	public ExecutionState init() {
		status = ExecutionState.READY;
		return status;

	}

	@Override
	public ExecutionState close() {
		status = ExecutionState.CLOSED;
		return status;
	}

}