package it.polimi.processing.events;

import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.results.EventResult;
import it.polimi.services.SQLListeService;
import lombok.Data;

@Data
public class Experiment implements Event, EventResult {

	private int experimentNumber;
	private String experimentName;
	private String engine, inputFileName, outputFileName, windowFileName;
	private String date, type, timecontrol;
	private Long timestampStart;
	private String comment;
	private long timestampEnd;

	@Override
	public boolean save(String where) {
		String sql = "VALUES (" + "'" + experimentName + "'" + "," + "'" + experimentNumber + "'" + "," + "'" + date + "'" + "," + "'"
				+ timestampStart + "'" + "," + "'" + timestampEnd + "'" + "," + "'" + engine + "'" + "," + "'" + type + "'" + "," + "'" + timecontrol
				+ "'" + "," + "'" + inputFileName + "'" + "," + "'" + outputFileName + "')";
		return SQLListeService.write(SQLListeService.BASELINE_INSERT + sql);
	}

	public Experiment(int experimentNumber, String name, String engine, String inputFileName, String outputFileName, String windowFileName,
			String date, String type, String timecontrol, String comment) {
		this.experimentNumber = experimentNumber;
		this.engine = engine;
		this.experimentName = name;
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.windowFileName = windowFileName;
		this.date = date;
		this.type = type;
		this.timecontrol = timecontrol;
		this.comment = comment;
	}

}
