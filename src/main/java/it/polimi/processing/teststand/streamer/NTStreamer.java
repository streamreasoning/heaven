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
	public void stream(int tripleGraph, String engineName, String fileName,
			String inputfileName, BufferedReader br) throws IOException {

		Logger.getRootLogger().debug("Start Streaming");
		// TODO
		EventFactory<T> factory = null;
		if (!ExecutionStates.READY.equals(status)) {
			throw new WrongStatusTransitionException("Not Ready " + status);
		} else {
			int streamedEvents = 0, lineNumber = 0, tripleCount = 0;
			String line;
			Set<String[]> eventTriples = new HashSet<String[]>();

			while ((line = br.readLine()) != null) {
				lineNumber++; // new line reading
				status = ExecutionStates.RUNNING;
				String[] s = parse(line); // new line parsing
				Logger.getRootLogger().debug("S: " + Arrays.deepToString(s));

				if (tripleCount < tripleGraph - 1) {
					eventTriples.add(s);
					tripleCount++;
				} else {
					eventTriples.add(s);

					streamedEvents += sendEvent(engineName, fileName,
							lineNumber, line, eventTriples, tripleGraph,
							factory);

					if (streamedEvents % 1000 == 0) {
						Logger.getRootLogger().info(
								"STREAMED " + streamedEvents + "EVENTS");
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
		String[] s = Parser
				.parseTriple(line, "src/main/resources/data/", false);
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

	private int sendEvent(String engineName, String fileName, int lineNumber,
			String line, Set<String[]> eventTriples, int triplesModel,
			EventFactory<T> factory) {
		for (String[] s : eventTriples) {
			Logger.getRootLogger()
					.debug("tripleSet: " + Arrays.deepToString(s));
		}
		StreamingEvent streamingEvent = new StreamingEvent(eventTriples,
				lineNumber, fileName, engineName);
		streamingEvent.setGraphTriples(triplesModel);
		if (stand.sendEvent(streamingEvent)) {
			Logger.getRootLogger().debug("SEND NEW EVENT: " + line);
			status = ExecutionStates.READY;
			return 1;
		} else {
			status = ExecutionStates.READY;
			Logger.getRootLogger().info("Not Saved " + line);
			return 0;
		}

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