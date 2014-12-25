package it.polimi.processing.events;

import it.polimi.processing.collector.data.CollectableData;
import it.polimi.processing.collector.data.SQLStmt;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.interfaces.ExperimentResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Experiment implements Event, ExperimentResult {
	private int experimentNumber;
	private String descr, engine, inputFileName, outputFileName;
	private Long timestampStart;
	private String comment;
	private long timestampEnd;

	@Override
	public CollectableData getSQL() {
		return new SQLStmt("VALUES (" + "'" + "EXP_" + experimentNumber + "'" + "," + "'" + timestampStart + "'" + "," + "'" + timestampEnd + "'" + ","
				+ "'" + engine + "'" + "," + "'" + inputFileName + "'" + "," + "'" + outputFileName + "'" + "," + "'"
				+ outputFileName.replace("Result", "LOG") + "'" + ");");
	}

}
