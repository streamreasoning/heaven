package it.polimi.processing.events.result;

import it.polimi.processing.collector.saver.data.CSV;
import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.StreamingEvent;

import java.util.Arrays;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class StreamingEventResult extends Event {

	private StreamingEvent inputEvent;
	private Set<String[]> all_triples;
	private long resultTimestamp;
	private double memory;

	public String getStartTripleEvent() {
		String starttriples = "[";
		for (String[] startTriple : inputEvent.getEventTriples()) {
			starttriples += Arrays.deepToString(startTriple);
		}
		starttriples += "]";
		return starttriples;
	}

	public int[] getLineNumbers() {
		return inputEvent.getLineNumbers();
	}

	public Set<String[]> getStartTriples() {
		return inputEvent.getEventTriples();
	}

	public Set<String[]> getTriples() {
		return all_triples;
	}

	/**
	 * <http://example.org/bob> { _:a foaf:name "Bob" . _:a foaf:mbox
	 * <mailto:bob@oldcorp.example.org> . _:a foaf:knows _:b . }
	 * 
	 * 
	 * **/
	public CollectableData getTrig() {
		return new TriG(inputEvent.getId(), all_triples);
	}

	public CollectableData getCSV() {

		String lines = ",";
		for (int p : inputEvent.getLineNumbers()) {
			lines += p + ",";
		}

		long queryLatency = resultTimestamp - inputEvent.getTimestamp();
		String s = inputEvent.getId() + lines + inputEvent.getTimestamp() + "," + inputEvent.getMemory() + "," + memory + "," + queryLatency;
		return new CSV(s);
	}
}
