package it.polimi.processing.events;

import it.polimi.processing.events.interfaces.Event;
import it.polimi.utils.Memory;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class RSPEvent implements Event {

	private String id;
	private Set<TripleContainer> eventTriples;
	private int eventNumber, experimentNumber;
	private long inputTimestamp;
	private double memoryBR;

	public RSPEvent(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber) {
		this.id = id;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
		this.memoryBR = Memory.getMemoryUsage();
	}

	public void reset(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber) {
		this.id = id;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
		this.memoryBR = Memory.getMemoryUsage();
	}

}
