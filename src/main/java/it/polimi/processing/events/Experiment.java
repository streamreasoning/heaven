package it.polimi.processing.events;

import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.services.SQLListeService;
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
	public boolean saveSQL(String where) {
		String sql = "VALUES (" + "'" + "EXP_" + experimentNumber + "'" + "," + "'" + timestampStart + "'" + "," + "'" + timestampEnd + "'" + ","
				+ "'" + engine + "'" + "," + "'" + inputFileName + "'" + "," + "'" + outputFileName + "'" + "," + "'"
				+ outputFileName.replace("Result", "LOG") + "'" + ");";
		return SQLListeService.write(sql);
	}

}
