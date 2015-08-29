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

	private String id;
	private Set<TripleContainer> eventTriples;
	private int eventNumber, experimentNumber;
	private long inputTimestamp;

	public int size() {
		return eventTriples.size();
	}

	public Stimulus(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber) {
		this.id = id;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
	}

	public Stimulus rebuild(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber) {
		this.id = id;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
		return this;
	}

}
