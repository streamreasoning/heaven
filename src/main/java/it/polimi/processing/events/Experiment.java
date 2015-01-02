package it.polimi.processing.events;

import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.results.EventResult;
import it.polimi.services.SQLListeService;
import lombok.Data;

@Data
public class Experiment implements Event, EventResult {

	private int experimentNumber;
	private String descr, engine, inputFileName, outputFileName, windowFileName;
	private Long timestampStart;
	private String comment;
	private long timestampEnd;

	public Experiment(int experimentNumber, String descr, String engine, String inputFileName, String outputFileName, String windowFileName) {
		this.experimentNumber = experimentNumber;
		this.descr = descr;
		this.engine = engine;
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.windowFileName = windowFileName;
	}

	@Override
	public boolean save(String where) {
		String sql = "VALUES (" + "'" + "EXP_" + experimentNumber + "'" + "," + "'" + timestampStart + "'" + "," + "'" + timestampEnd + "'" + ","
				+ "'" + engine + "'" + "," + "'" + inputFileName + "'" + "," + "'" + outputFileName + "'" + "," + "'"
				+ outputFileName.replace("Result", "LOG") + "'" + ");";
		return SQLListeService.write(sql);
	}

}
