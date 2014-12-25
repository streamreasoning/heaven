package it.polimi.processing.events;

import it.polimi.processing.events.interfaces.Event;
import it.polimi.services.system.Memory;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class RSPTripleSet implements Event {

	private String id;
	private Set<TripleContainer> eventTriples = new HashSet<TripleContainer>();
	private int eventNumber, experimentNumber;
	private long inputTimestamp;
	private double memoryBR;

	public int size() {
		return eventTriples.size();
	}

	public RSPTripleSet(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber) {
		this.id = id;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
		this.memoryBR = Memory.getMemoryUsage();
	}

	public RSPTripleSet rebuild(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber) {
		this.id = id;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
		this.memoryBR = Memory.getMemoryUsage();
		return this;
	}

}
