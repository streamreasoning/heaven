package it.polimi.processing.teststand.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.streamer.Parser;
import it.polimi.processing.streamer.RSPEventStreamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

/**
 * @author Riccardo
 * 
 * @param <T>
 *            The kind of class thar will received the streamed events in this
 *            implementation it extends EventProcessor which offers sendEvent(E
 *            e) method. This class is paramtric too, and the kind of event
 *            processed at this time is StreamingEvent
 */

@Getter
@Log4j
public class NTStreamer extends RSPEventStreamer {

	/**
	 * Represents the core of the streaming procedure, is must publish the
	 * sendEvent method through which is possibile to inject any kind of event
	 * into the system
	 */
	private RSPEvent lastEvent;

	public NTStreamer(EventProcessor<RSPEvent> processor) {
		super(processor, ExecutionState.CLOSED);
	}

	/**
	 * Open the buffer reader that incapsulate a csv form input file
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

		log.debug("Start Streaming");

		if (!ExecutionState.READY.equals(status)) {
			throw new WrongStatusTransitionException("Not Ready " + status);
		} else {
			int streamedEvents = 0;
			String line;
			Set<String[]> eventTriples = new HashSet<String[]>();

			while ((line = br.readLine()) != null) {
				status = ExecutionState.RUNNING;
				String[] s = parse(line);
				log.debug("S: " + Arrays.deepToString(s));

				eventTriples.add(s);
				if (sendEvent(eventTriples, experimentNumber, streamedEvents)) {

					log.debug("SEND NEW EVENT: " + line);
					status = ExecutionState.READY;
					streamedEvents++;

				} else {
					status = ExecutionState.READY;
					log.info("Not Saved " + line);

				}

				if (streamedEvents % 1000 == 0) {
					log.info("STREAMED " + streamedEvents + "EVENTS");
				}

				eventTriples = new HashSet<String[]>();

			}
			log.info("Number of Events: " + streamedEvents);
			br.close();

		}
	}

	private String[] parse(String line) {
		String[] s = Parser.parseTriple(line, "src/main/resources/data/", false);
		if (s.length > 3) {
			throw new IllegalArgumentException("Too much arguments");
		}

		log.debug("S: " + Arrays.deepToString(s));
		s[0] = s[0].replace("<", "");
		s[0] = s[0].replace(">", "");

		s[1] = s[1].replace("<", "");
		s[1] = s[1].replace(">", "");

		s[2] = s[2].replace("<", "");
		s[2] = s[2].replace(">", "");
		return s;
	}

	private boolean sendEvent(Set<String[]> eventTriples, int experimentNumber, int eventNumber) {
		for (String[] s : eventTriples) {
			log.debug("tripleSet: " + Arrays.deepToString(s));
		}

		String id = "<http://example.org/" + experimentNumber + "/" + eventNumber + ">";

		return processor.process(lastEvent = createEvent(id, eventTriples, experimentNumber, eventNumber));

	}

	@Override
	public ExecutionState init() {
		return status = ExecutionState.READY;

	}

	@Override
	public ExecutionState close() {
		return status = ExecutionState.CLOSED;
	}

}