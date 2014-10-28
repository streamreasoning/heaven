package it.polimi.processing.events.result;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.collector.saver.data.SQL;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.Experiment;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ExperimentResultEvent extends Event {

	private Experiment experiment;
	private long timestamp_result;

	/**
	 * This toString Element is used for database insertion (EXP_ID, TS_INIT,
	 * TS_END, ENGINE, INPUT_FILE,RESULT_FILE, FILE_LOG_FOLDER)
	 * 
	 * **/
	public CollectableData getSQL() {
		return new SQL("VALUES (" + "'" + "EXP_" + experiment.getExperimentNumber() + "'" + "," + "'" + experiment.getTimestamp() + "'" + "," + "'"
				+ timestamp_result + "'" + "," + "'" + experiment.getEngine() + "'" + "," + "'" + experiment.getInputFileName() + "'" + "," + "'"
				+ experiment.getOutputFileName() + "'" + "," + "'" + experiment.getOutputFileName().replace("Result", "LOG") + "'" + ");");
	}

}
