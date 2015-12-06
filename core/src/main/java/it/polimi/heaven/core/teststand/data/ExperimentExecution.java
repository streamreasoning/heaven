package it.polimi.heaven.core.teststand.data;

import it.polimi.services.SQLListeService;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@Data
@Log4j
public class ExperimentExecution {

	private Experiment e;
	private Long timestampStart;
	private Long timestampEnd;

	public ExperimentExecution(Experiment e, Long timestampStart) {
		this.e = e;
		this.timestampStart = timestampStart;
	}

	public boolean save(String where) {
		String type = "";
		type += e.isMemoryLog() ? "MEM" : "";
		type += e.isLatencyLog() ? "LAT" : "";

		;

		String sql = "VALUES (" + "'" + getName() + "'" + "," + "'" + e.getExperimentNumber() + "'" + "," + "'" + e.getDate() + "'" + "," + "'"
				+ timestampStart + "'" + "," + "'" + timestampEnd + "'" + "," + "'" + e.getEngine() + "'" + "," + "'" + type + "'" + "," + "'"
				+ e.getTimecontrol() + "'" + "," + "'" + e.getInputSource() + "'" + "," + "'" + e.getOutputPath() + "/" + getName() + "')";
		log.info("Insert Experiment [" + sql + "] in [" + where + "]");
		return SQLListeService.write(SQLListeService.BASELINE_INSERT + sql);
	}

	public String getName() {
		return "EN" + e.getExperimentNumber() + "EXE" + e.getExecutionNumber() + "_" + e.getDate() + "_" + e.getMetadata();
	}

}
