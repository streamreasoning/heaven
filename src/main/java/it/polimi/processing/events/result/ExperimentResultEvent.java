package it.polimi.processing.events.result;

import it.polimi.processing.collector.saver.data.SQL;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.Experiment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * decorate Experiment Event with results
 * 
 * @author Riccardo
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExperimentResultEvent extends Event {

	private String inputFile, resultFile, logFileName;
	private long timestamp_end;
	private String name;
	private Experiment experiment;

	public ExperimentResultEvent(Experiment e) {
		this.experiment = e;
		this.timestamp_end = System.currentTimeMillis();
		this.name = "result_" + e.getName();

	}

	/**
	 * This toString Element is used for database insertion (EXP_ID, TS_INIT,
	 * TS_END, INPUT_FILE,RESULT_FILE, FILE_LOG_FOLDER)
	 * 
	 * **/
	public SQL getSQL() {
		return new SQL("VALUES (" + "'" + experiment.getName() + "'" + ","
				+ "'" + experiment.getTimestamp() + "'" + "," + "'"
				+ timestamp_end + "'" + "," + "'"
				+ experiment.getInputFileName() + "'" + "," + "'"
				+ experiment.getOutputFileName() + "'" + "," + "'"
				+ logFileName + "'" + ");");
	}

}
