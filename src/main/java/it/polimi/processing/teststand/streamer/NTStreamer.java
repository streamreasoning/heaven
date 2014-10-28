package it.polimi.processing.teststand.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.EventFactory;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.streamer.Parser;
import it.polimi.processing.streamer.Streamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import org.apache.log4j.Logger;

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
public class NTStreamer<T extends Event> implements Streamer {

	/**
	 * Represents the core of the streaming procedure, is must publish the
	 * sendEvent method through which is possibile to inject any kind of event
	 * into the system
	 */
	private final EventProcessor<StreamingEvent> stand;
	private ExecutionStates status;

	public NTStreamer(EventProcessor<StreamingEvent> stand) {
		this.stand = stand;
		this.status = ExecutionStates.CLOSED;
	}

	/**
	 * Open the buffer reader that incapsulate a csv form input file
	 * 
	 * 
	 * @param status
	 *            represent the execution state of the Envirorment outside the
	 *            Streamer. Is exploit the dependecy injection paradince since
	 *            the streamer must be controlled by the outstanding application
	 *            system
	 **/
	@Override
	public void stream(BufferedReader br, int experimentNumber, String engineName, int tripleGraph) throws IOException {

		Logger.getRootLogger().debug("Start Streaming");
		EventFactory<T> factory = null;
		if (!ExecutionStates.READY.equals(status)) {
			throw new WrongStatusTransitionException("Not Ready " + status);
		} else {
			int streamedEvents = 0, lineNumber = 0, tripleCount = 0;
			int[] lineNumbers = new int[tripleGraph];
			String line;
			Set<String[]> eventTriples = new HashSet<String[]>();

			while ((line = br.readLine()) != null) {
				lineNumber++; // new line reading
				status = ExecutionStates.RUNNING;
				String[] s = parse(line); // new line parsing
				Logger.getRootLogger().debug("S: " + Arrays.deepToString(s));
				if (tripleCount < tripleGraph - 1) {
					eventTriples.add(s);
					lineNumbers[tripleCount] = lineNumber;
					tripleCount++;
				} else {
					eventTriples.add(s);
					lineNumbers[tripleCount] = lineNumber;
					if (sendEvent(eventTriples, tripleGraph, streamedEvents, experimentNumber, engineName, lineNumbers, line, factory)) {

						Logger.getRootLogger().debug("SEND NEW EVENT: " + line);
						status = ExecutionStates.READY;
						streamedEvents++;

						if (streamedEvents % 1000 == 0) {
							Logger.getRootLogger().info("STREAMED " + streamedEvents + "EVENTS");
						}
					} else {
						status = ExecutionStates.READY;
						Logger.getRootLogger().info("Not Saved " + line);

					}

					tripleCount = 0;
					eventTriples = new HashSet<String[]>();

				}

			}
			Logger.getRootLogger().info("Number of Events: " + streamedEvents);
			br.close();

		}
	}

	private String[] parse(String line) {
		String[] s = Parser.parseTriple(line, "src/main/resources/data/", false);
		if (s.length > 3) {
			throw new IllegalArgumentException("Too much arguments");
		}

		Logger.getRootLogger().debug("S: " + Arrays.deepToString(s));
		s[0] = s[0].replace("<", "");
		s[0] = s[0].replace(">", "");

		s[1] = s[1].replace("<", "");
		s[1] = s[1].replace(">", "");

		s[2] = s[2].replace("<", "");
		s[2] = s[2].replace(">", "");
		return s;
	}

	private boolean sendEvent(Set<String[]> eventTriples, int tripleGraph, int eventNumber, int experimentNumber, String engineName,
			int[] lineNumbers, String line, EventFactory<T> factory) {
		for (String[] s : eventTriples) {
			Logger.getRootLogger().debug("tripleSet: " + Arrays.deepToString(s));
		}

		String id = "<http://example.org/" + experimentNumber + "/" + eventNumber + ">";

		StreamingEvent streamingEvent = new StreamingEvent(eventTriples, id, eventNumber, experimentNumber, tripleGraph, lineNumbers,
				System.currentTimeMillis());

		return stand.sendEvent(streamingEvent);
	}

	@Override
	public ExecutionStates init() {
		return status = ExecutionStates.READY;

	}

	@Override
	public ExecutionStates close() {
		return status = ExecutionStates.CLOSED;
	}

}