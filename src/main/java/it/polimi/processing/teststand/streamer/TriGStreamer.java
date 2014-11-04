package it.polimi.processing.teststand.streamer;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.streamer.Parser;
import it.polimi.processing.streamer.Streamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;
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
public class TriGStreamer<T extends Event> implements Streamer<T> {

	/**
	 * Represents the core of the streaming procedure, is must publish the
	 * sendEvent method through which is possibile to inject any kind of event
	 * into the system
	 */
	private final EventProcessor<StreamingEvent> stand;
	@Setter
	private ExecutionStates status;

	public TriGStreamer(EventProcessor<StreamingEvent> stand) {
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

		log.debug("Start Streaming");
		if (!ExecutionStates.READY.equals(status)) {
			throw new WrongStatusTransitionException("Not Ready " + status);
		} else {
			int streamedEvents = 0, graphNumber = 0;
			String line;
			TriG eventTrig = null;
			String triGLine = "";

			while ((line = br.readLine()) != null) {

				if (triGLine.equals(""))
					triGLine += line;
				else {
					triGLine += Parser.EOF + line;
				}
				if (!Parser.triGMatch(triGLine)) {
					continue;
				} else {
					graphNumber++;
					eventTrig = Parser.parseTrigGraph(triGLine);
					status = ExecutionStates.RUNNING;

					if (sendEvent(eventTrig, tripleGraph, streamedEvents, experimentNumber, new int[] { graphNumber })) {

						log.debug("SEND NEW EVENT: " + line);

						status = ExecutionStates.READY;
						streamedEvents++;
						triGLine = "";
					} else {
						status = ExecutionStates.READY;
						log.info("Not Saved " + line);

					}

					if (streamedEvents % 1000 == 0) {
						log.info("STREAMED " + streamedEvents + "EVENTS");
					}

				}

			}
			br.close();

		}
	}

	private boolean sendEvent(TriG trig, int tripleGraph, int eventNumber, int experimentNumber, int[] graphNumber) {
		StreamingEvent streamingEvent = new StreamingEvent(trig.getKey(), new HashSet<String[]>(trig.getTriples()), eventNumber, experimentNumber,
				tripleGraph, graphNumber, System.currentTimeMillis());
		return stand.sendEvent(streamingEvent);
	}

	@Override
	public ExecutionStates init() {
		status = ExecutionStates.READY;
		return status;

	}

	@Override
	public ExecutionStates close() {
		status = ExecutionStates.CLOSED;
		return status;
	}

}