package it.polimi.processing.events;

import it.polimi.processing.events.interfaces.Event;
import it.polimi.utils.Memory;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class RSPEvent implements Event {

	private String id;
	private Set<String[]> eventTriples;
	private int eventNumber, experimentNumber;
	private long inputTimestamp;
	private double memoryBR;

	public RSPEvent(String id, Set<String[]> hashSet, int eventNumber, int experimentNumber) {
		this.id = id;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
		this.memoryBR = Memory.getMemoryUsage();
	}

}
