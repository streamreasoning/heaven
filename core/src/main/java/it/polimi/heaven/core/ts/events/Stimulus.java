package it.polimi.heaven.core.ts.events;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Stimulus implements Event {

	private String id, streamName;
	private Set<TripleContainer> eventTriples;
	private int eventNumber, experimentNumber;
	private long inputTimestamp, timestamp;

	public int size() {
		return eventTriples.size();
	}

	public Stimulus(String id, String streamName, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber, long timestamp) {
		this.id = id;
		this.streamName = streamName;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
		this.timestamp = timestamp;
	}

	public Stimulus rebuild(String id, String streamName, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber, long timestamp) {
		this.id = id;
		this.streamName = streamName;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
		this.timestamp = timestamp;
		return this;
	}

}
