package it.polimi.processing.events;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StreamingEvent extends Event {

	private Set<String[]> eventTriples;
	private String id, fileName, engine;
	private long timestamp;
	private int eventNumber, graphTriples;
	private int[] lineNumbers;

	public StreamingEvent(Set<String[]> eventTriples, int[] lineNumbers,
			int eventNumber, String fileName, String engine) {
		timestamp = System.currentTimeMillis();
		this.eventTriples = eventTriples;
		this.engine = engine;
		this.id = "<http://example.org/" + eventNumber + ">";
		this.lineNumbers = lineNumbers;
		this.fileName = fileName;
	}

}
