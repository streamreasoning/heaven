package it.polimi.comparator.events;

import it.polimi.events.Event;



public class ComparisonExperimentResult extends Event  {

	private String exp_id, inputFile, resultFile, logFolderPath;
	private long timestamp_init;
	private long timestamp_end;

	public long getTimestamp_end() {
		return timestamp_end;
	}

	public void setTimestamp_end(long timestamp_end) {
		this.timestamp_end = timestamp_end;
	}

	public long getTimestamp_init() {
		return timestamp_init;
	}

	public ComparisonExperimentResult(String inputFile, String resultFile,
			String logFolderPath) {
		timestamp_init = System.currentTimeMillis();
		this.inputFile = inputFile;
		this.resultFile = resultFile;
		this.logFolderPath = logFolderPath;
		this.exp_id = "EXPERIMENT_ON_"
				+ inputFile.toUpperCase()
						.substring(0, inputFile.length() - 4);

	}

	@Override
	/**
	 * (EXP_ID, TS_INIT, TS_END, INPUT_FILE,RESULT_FILE, FILE_LOG_FOLDER)
	 * 
	 * **/
	public String toString() {
		return "VALUES (" + "'" + exp_id + "'" + "," + "'" + timestamp_init
				+ "'" + "," + "'" + timestamp_end + "'" + "," + "'" + inputFile
				+ "'" + "," + "'" + resultFile + "'" + "," + "'"
				+ logFolderPath + "'" + ");";
	}


}
