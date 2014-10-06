package it.polimi.teststand.events;

import java.util.Arrays;
import java.util.Set;

public class StreamingEvent extends Event {

	@Override
	public String toString() {
		return "StreamingEvent [eventTriples=" + getTripleToString() + ", id="
				+ id + ", event_timestamp=" + event_timestamp + ", ignore="
				+ ignore + "]";
	}

	public String getTripleToString() {
		String triples = "";
		for (String[] triple : eventTriples) {
			triples += Arrays.deepToString(triple)
					+ System.getProperty("line.separator");

		}
		return triples;
	}

	private Set<String[]> eventTriples;
	private String id;
	private long event_timestamp;
	private boolean ignore = false;
	private int lineNumber;

	public StreamingEvent(Set<String[]> eventTriples, boolean ignore,
			int lineNumber) {
		event_timestamp = System.currentTimeMillis();
		this.id = "<http://example.org/" + event_timestamp + ">";
		this.ignore = ignore;
		this.lineNumber = lineNumber;
		this.eventTriples = eventTriples;
	}

	public StreamingEvent(Set<String[]> eventTriples, int lineNumber) {
		this.lineNumber = lineNumber;
		event_timestamp = System.currentTimeMillis();
		this.id = "<http://example.org/" + event_timestamp + ">";
		this.eventTriples = eventTriples;
	}

	public long getEvent_timestamp() {
		return event_timestamp;
	}

	public void setEvent_timestamp(long event_timestamp) {
		this.event_timestamp = event_timestamp;
	}

	public String getId() {
		return id;
	}

	public Set<String[]> getEventTriples() {
		return eventTriples;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public int getLineNumber() {
		return lineNumber;
	}

}
