package it.polimi.teststand.events;

import it.polimi.events.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TestExperimentResultEvent extends Event {

	private String inputFile, resultFile, logFolderPath;
	private long timestamp_init;
	private long timestamp_end;

	public TestExperimentResultEvent(String inputFile, String resultFile,
			String logFolderPath, String exp_id) {
		timestamp_init = System.currentTimeMillis();
		this.inputFile = inputFile;
		this.resultFile = resultFile;
		this.logFolderPath = logFolderPath;
		setName(exp_id);

	}

	@Override
	/**
	 *  This toString Element is used for database insertion
	 * (EXP_ID, TS_INIT, TS_END, INPUT_FILE,RESULT_FILE, FILE_LOG_FOLDER)
	 * 
	 * **/
	public String toString() {
		return "VALUES (" + "'" + getName() + "'" + "," + "'" + timestamp_init
				+ "'" + "," + "'" + timestamp_end + "'" + "," + "'" + inputFile
				+ "'" + "," + "'" + resultFile + "'" + "," + "'"
				+ logFolderPath + "'" + ");";
	}

}
