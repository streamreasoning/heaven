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
import java.util.List;

import lombok.Getter;
import lombok.Setter;
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
public class TriGStreamer<T extends Event> implements Streamer<T> {

	/**
	 * Represents the core of the streaming procedure, is must publish the
	 * sendEvent method through which is possibile to inject any kind of event
	 * into the system
	 */
	private final EventProcessor<StreamingEvent> stand;
	@Setter
	private ExecutionStates status;
	private String line;
	private TriG eventTrig = null;
	private String currentTriG = "";
	private StreamingEvent streamingEvent;

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

		if (!ExecutionStates.READY.equals(status)) {
			throw new WrongStatusTransitionException("Not Ready " + status);
		} else {
			int streamedEvents = 0, graphNumber = 0;
			int count = 0;
			while ((line = br.readLine()) != null && !line.isEmpty()) {

				count++;

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

					graphNumber++;
					eventTrig = Parser.parseTrigGraph(currentTriG);
					status = ExecutionStates.RUNNING;

					if (sendEvent(eventTrig, tripleGraph, streamedEvents, experimentNumber, new int[] { graphNumber })) {
						log.debug("Send [ " + line + " ] as a New StreamEvent");

						status = ExecutionStates.READY;
						streamedEvents++;
						resetCurrentTrig();

					} else {
						status = ExecutionStates.READY;
						log.debug("Not Saved " + line);

					}

					log.debug("Total Graph Size " + count);
					count = 0;

					if (streamedEvents % 1000 == 0) {
						log.info("STREAMED " + streamedEvents + "EVENTS");
					}

				}

			}
			br.close();

		}
	}

	private void resetCurrentTrig() {
		currentTriG = "";
	}

	private boolean sendEvent(TriG trig, int tripleGraph, int eventNumber, int experimentNumber, int[] graphNumber) {

		String key = trig.getKey();
		List<String[]> triples = trig.getTriples();

		if (trig != null && key != null && !key.isEmpty() && triples != null && triples.size() > 0) {
			if (streamingEvent == null)
				streamingEvent = new StreamingEvent(key, new HashSet<String[]>(triples), eventNumber, experimentNumber, tripleGraph, graphNumber,
						System.currentTimeMillis());
			else {
				streamingEvent.setId(key);
				streamingEvent.setEventTriples(new HashSet<String[]>(triples));
				streamingEvent.setEventNumber(eventNumber);
				streamingEvent.setExperimentNumber(experimentNumber);
				streamingEvent.setTripleGraph(tripleGraph);
				streamingEvent.setLineNumbers(graphNumber);
				streamingEvent.setTimestamp(System.currentTimeMillis());
			}

			return stand.sendEvent(streamingEvent);
		}
		return false;
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