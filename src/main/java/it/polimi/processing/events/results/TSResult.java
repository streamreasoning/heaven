package it.polimi.processing.events.results;

import it.polimi.processing.collector.data.CSV;
import it.polimi.processing.collector.data.CollectableData;
import it.polimi.processing.collector.data.TriG;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TSResult implements EventResult {

	private String id;
	private Result result;
	private int eventNumber;
	private long inputTimestamp;
	private double memoryB;
	private double memoryA;
	private Boolean cs, ss, cr, sr;

	@Override
	public CollectableData getTrig() {
		return new TriG(id, result.getStatements());
	}

	@Override
	public CollectableData getCSV() {
		// TODO instance of Result
		String compleAndSoundSIMPL = (cs != null && ss != null) ? "," + cs + "," + ss : "";
		String compleAndSoundRHODF = (cr != null && sr != null) ? "," + cr + "," + sr : "";

		String s = id + "," + eventNumber + "," + memoryB + "," + memoryA + "," + (result.getTimestamp() - inputTimestamp) + compleAndSoundSIMPL
				+ compleAndSoundRHODF;
		return new CSV(s);
	}

	public int size() {
		return result.size();
	}
}
