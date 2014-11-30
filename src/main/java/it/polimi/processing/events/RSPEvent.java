package it.polimi.processing.events;

import it.polimi.processing.collector.saver.data.CSV;
import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.utils.Memory;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class RSPEvent implements Event, EventResult {

	private String id;
	private Set<String[]> eventTriples;
	private int eventNumber, experimentNumber;
	private long inputTimestamp;
	private double memoryBR;

	private Set<String[]> all_triples;
	private long resultTimestamp;
	private double memoryAR;

	public RSPEvent(String id, Set<String[]> hashSet, int eventNumber, int experimentNumber) {
		this.id = id;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.inputTimestamp = System.currentTimeMillis();
		this.memoryBR = Memory.getMemoryUsage();
	}

	@Override
	public CollectableData getTrig() {
		return new TriG(id, all_triples);
	}

	@Override
	public CollectableData getCSV() {
		String lines = ",";

		long queryLatency = resultTimestamp - inputTimestamp;
		String s = id + lines + inputTimestamp + "," + memoryBR + "," + memoryAR + "," + queryLatency;
		return new CSV(s);
	}

}
