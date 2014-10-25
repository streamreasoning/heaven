package it.polimi.processing.events.result;

import it.polimi.processing.collector.Collectable;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.Experiment;

import java.util.Set;

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
public class ExperimentResultEvent extends Event implements Collectable {

	private String inputFile, resultFile, logFileName;
	private long timestamp_end;
	private String name;
	private Experiment experiment;

	public ExperimentResultEvent(Experiment e) {
		this.experiment = e;
		this.timestamp_end = System.currentTimeMillis();
		this.name = "result_" + e.getName();

	}

	@Override
	public Set<String[]> getTriples() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTrig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCSV() {
		return null;
	}

	/**
	 * This toString Element is used for database insertion (EXP_ID, TS_INIT,
	 * TS_END, INPUT_FILE,RESULT_FILE, FILE_LOG_FOLDER)
	 * 
	 * **/
	@Override
	public String getSQL() {
		return "VALUES (" + "'" + experiment.getName() + "'" + "," + "'"
				+ experiment.getTimestamp() + "'" + "," + "'" + timestamp_end
				+ "'" + "," + "'" + experiment.getInputFileName() + "'" + ","
				+ "'" + experiment.getOutputFileName() + "'" + "," + "'"
				+ logFileName + "'" + ");";
	}

	@Override
	public byte[] getBytes() {
		return toString().getBytes();
	}

	@Override
	public String getName() {
		return "/" + experiment.getEngine() + "/";
	}
}
