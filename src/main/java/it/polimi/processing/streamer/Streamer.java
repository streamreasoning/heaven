package it.polimi.processing.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.exceptions.WrongStatusTransitionException;

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
public class Streamer implements Startable<ExecutionStates> {

	/**
	 * Represents the core of the streaming procedure, is must publish the
	 * sendEvent method through which is possibile to inject any kind of event
	 * into the system
	 */
	private EventProcessor<StreamingEvent> stand;
	private ExecutionStates status;

	public Streamer(EventProcessor<StreamingEvent> stand) {
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
	public void stream(int tripleGraph, String engineName, String fileName,
			BufferedReader br) throws IOException {

		Logger.getRootLogger().debug("Start Streaming");

		if (!ExecutionStates.READY.equals(status)) {
			throw new WrongStatusTransitionException("Not Ready");
		} else {
			int streamedEvents = 0, lineNumber = 0;
			String line;
			Set<String[]> eventTriples = new HashSet<String[]>();
			int i = 0;
			while ((line = br.readLine()) != null) {
				status = ExecutionStates.RUNNING;
				lineNumber++;
				String[] s = parse(line);
				Logger.getRootLogger().debug("S: " + Arrays.deepToString(s));
				if (i < tripleGraph - 1) {
					eventTriples.add(s);
					i++;
				} else {
					eventTriples.add(s);
					streamedEvents = sendEvent(engineName, fileName,
							streamedEvents, lineNumber, line, eventTriples);
					i = 1;
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

	private int sendEvent(String engineName, String fileName,
			int streamedEvents, int lineNumber, String line,
			Set<String[]> eventTriples) {
		for (String[] s : eventTriples) {
			Logger.getRootLogger()
					.debug("tripleSet: " + Arrays.deepToString(s));
		}
		if (stand.sendEvent(new StreamingEvent(eventTriples, lineNumber,
				fileName, engineName))) {
			streamedEvents++;
		} else {
			Logger.getRootLogger().info("Not Saved " + line);
		}
		status = ExecutionStates.READY;
		if (streamedEvents % 1000 == 0) {
			Logger.getRootLogger()
					.info("STREAMED " + streamedEvents + "EVENTS");
		}
		Logger.getRootLogger().debug("SEND NEW EVENT: " + line);
		return streamedEvents;
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