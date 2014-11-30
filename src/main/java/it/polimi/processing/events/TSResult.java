package it.polimi.processing.events;

import it.polimi.processing.collector.saver.data.CSV;
import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.events.interfaces.EventResult;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TSResult implements EventResult {

	private String id;
	private int eventNumber;
	private Set<String[]> statements;
	private long inputTimestamp, outputTimestamp;
	private double memoryA;
	private double memoryB;
	private Boolean cs, ss, cr, sr;

	@Override
	public CollectableData getTrig() {
		return new TriG(id, statements);
	}

	@Override
	public CollectableData getCSV() {
		String compleAndSoundSIMPL = (cs != null && ss != null) ? "," + cs + "," + ss : "";
		String compleAndSoundRHODF = (cr != null && sr != null) ? "," + cr + "," + sr : "";

		String s = id + "," + eventNumber + "," + memoryB + "," + memoryA + "," + (outputTimestamp - inputTimestamp) + compleAndSoundSIMPL
				+ compleAndSoundRHODF;
		return new CSV(s);
	}
}
