package it.polimi.processing.events;

import it.polimi.processing.events.interfaces.Event;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class InputRDFStream implements Event {

	private String id;
	private Set<TripleContainer> eventTriples;
	private int eventNumber, experimentNumber;
	private long inputTimestamp;

	public int size() {
		return eventTriples.size();
	}

	public InputRDFStream(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber) {
		this.id = id;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
	}

	public InputRDFStream rebuild(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber) {
		this.id = id;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
		return this;
	}

}
