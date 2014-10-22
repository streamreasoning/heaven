package it.polimi.processing.events;

import java.util.Arrays;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StreamingEvent extends Event {

	private Set<String[]> eventTriples;
	private String id, fileName;
	private long timestamp;
	private String engine;
	private int lineNumber;

	public StreamingEvent(Set<String[]> eventTriples, int lineNumber,
			String fileName, String engine) {
		timestamp = System.currentTimeMillis();
		this.eventTriples = eventTriples;
		this.engine = engine;
		long key = 0;
		for (String[] triple : eventTriples) {
			key += Arrays.deepToString(triple).hashCode();

		}
		this.id = "<http://example.org/" + key + ">";
		this.lineNumber = lineNumber;
		this.fileName = fileName;
	}

	public String getTripleToString() {
		String triples = "";
		for (String[] triple : eventTriples) {
			triples += Arrays.deepToString(triple)
					+ System.getProperty("line.separator");

		}
		return triples;
	}

}
