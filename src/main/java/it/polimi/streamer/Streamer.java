package it.polimi.streamer;

import it.polimi.EventProcessor;
import it.polimi.enums.ExecutionStates;
import it.polimi.events.StreamingEvent;
import it.polimi.teststand.exceptions.WrongStatusTransitionException;
import it.polimi.utils.RDFSUtils;

import java.io.BufferedReader;
import java.io.IOException;
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
public class Streamer {

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
	public void stream(String fileName, BufferedReader br) throws IOException {

		Logger.getRootLogger().debug("Start Streaming");

		if (!ExecutionStates.READY.equals(status)) {
			throw new WrongStatusTransitionException("Not Ready");
		} else {
			int streamedEvents = 0, lineNumber = 0;
			String line;
			Set<String[]> eventTriples;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				String[] s = Parser.parseTriple(line,
						"src/main/resources/data/", false);
				if (s.length > 3) {
					throw new IllegalArgumentException("Too much arguments");
				}

				s[0] = s[0].replace("<", "");
				s[0] = s[0].replace(">", "");

				s[1] = s[1].replace("<", "");
				s[1] = s[1].replace(">", "");

				s[2] = s[2].replace("<", "");
				s[2] = s[2].replace(">", "");

				if (RDFSUtils.isDatatype(s[1]) || RDFSUtils.isType(s[1])) {
					continue; // TODO spostare in fase di setup
				} else {
					Logger.getRootLogger().debug("SEND NEW EVENT: " + line);

					status = ExecutionStates.RUNNING;
					eventTriples = new HashSet<String[]>();
					eventTriples.add(s);
					if (stand.sendEvent(new StreamingEvent(eventTriples,
							lineNumber, fileName))) {
						streamedEvents++;
					} else {
						Logger.getRootLogger().info("Not Saved " + line);
					}
					status = ExecutionStates.READY;
				}

			}
			Logger.getRootLogger().info("Number of Events: " + streamedEvents);
			br.close();

		}
	}

	public ExecutionStates init() {
		return status = ExecutionStates.READY;

	}

	public ExecutionStates close() {
		return status = ExecutionStates.CLOSED;
	}

}