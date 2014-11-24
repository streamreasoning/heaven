package it.polimi.processing.events;

import it.polimi.processing.collector.saver.data.CSV;
import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.interfaces.EventResult;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class TestStandEvent implements Event, EventResult {

	private String id;
	private Set<String[]> eventTriples;
	private int eventNumber, experimentNumber;
	private int tripleGraph;
	private int[] lineNumbers;
	private long inputTimestamp;
	private double memoryBR;

	private Set<String[]> all_triples;
	private long resultTimestamp;
	private double memoryAR;

	public TestStandEvent(String key, HashSet<String[]> hashSet, int eventNumber, int experimentNumber, int tripleGraph, int[] graphNumber,
			long currentTimeMillis, double memoryUsage) {
		this.id = key;
		this.eventTriples = hashSet;
		this.eventNumber = eventNumber;
		this.experimentNumber = experimentNumber;
		this.tripleGraph = tripleGraph;
		this.lineNumbers = graphNumber;
		this.inputTimestamp = currentTimeMillis;
		this.memoryBR = memoryUsage;
	}

	@Override
	public CollectableData getTrig() {
		return new TriG(id, all_triples);
	}

	@Override
	public CollectableData getCSV() {
		String lines = ",";
		for (int p : lineNumbers) {
			lines += p + ",";
		}

		long queryLatency = resultTimestamp - inputTimestamp;
		String s = id + lines + inputTimestamp + "," + memoryBR + "," + memoryAR + "," + queryLatency;
		return new CSV(s);
	}

}
