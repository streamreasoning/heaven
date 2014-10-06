package it.polimi.streamer;

import it.polimi.teststand.enums.ExecutionStates;
import it.polimi.teststand.events.StreamingEvent;
import it.polimi.teststand.exceptions.WrongStatusTransitionException;
import it.polimi.teststand.utils.RDFSUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
public class Streamer<T extends EventProcessor<StreamingEvent>> {


	private T engine;

	public Streamer(T model) {
		this.engine = model;
	}


	/**
	 * Open the buffer reader that incapsulate a csv form input file
	 * 
	 * 
	 * @param status
	 *            represent the execution state of the Envirorment which exploit
	 *            the streamer the state transitions require a light to mantain
	 *            the consequetially execution of phases
	 **/
	public void stream(ExecutionStates status, BufferedReader br)
			throws IOException {

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
					continue;
				} else {
					Logger.getRootLogger().debug(line);
					status = ExecutionStates.SENDING;
					Logger.getRootLogger().debug("SEND NEW EVENT");
					eventTriples = new HashSet<String[]>();
					eventTriples.add(s);
					if (engine.sendEvent(new StreamingEvent(eventTriples,
							lineNumber))) {
						streamedEvents++;
					} else {
						Logger.getRootLogger().info("Not Saved");
					}
					status = ExecutionStates.READY;
				}

			}
			Logger.getRootLogger().info("Number of Events: " + streamedEvents);
			br.close();

		}
	}

}