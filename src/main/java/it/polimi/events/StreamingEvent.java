package it.polimi.events;

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
	private int lineNumber;

	public StreamingEvent(Set<String[]> eventTriples, int lineNumber,
			String fileName) {
		timestamp = System.currentTimeMillis();
		this.eventTriples = eventTriples;
		String key = "[";
		for (String[] triple : eventTriples) {
			key += Arrays.deepToString(triple);

		}
		key += "]";

		this.id = "<http://example.org/" + key.hashCode() + ">";
		this.lineNumber = lineNumber;
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "StreamingEvent [eventTriples=" + getTripleToString() + ", id="
				+ id + ", event_timestamp=" + timestamp + ", ignore=" + "]";
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
