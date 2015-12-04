package it.polimi.heaven.core.ts.events;

import it.polimi.services.SQLListeService;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@Data
@Log4j
public class Experiment implements Event, EventResult {

	private String date;
	private int experimentNumber;
	private int executionNumber;
	private String metadata;

	private String engine;
	private String timecontrol;
	private Long timestampStart;
	private Long timestampEnd;

	private boolean memoryLog;
	private boolean latencyLog;
	private boolean resultLog;
	private boolean aboxLog;

	private String inputSource;
	private String outputPath;

	// requirements

	private long responsivity;

	@Override
	public boolean save(String where) {
		String type = "";
		type += memoryLog ? "MEM" : "";
		type += latencyLog ? "LAT" : "";

		;

		String sql = "VALUES (" + "'" + getName() + "'" + "," + "'" + experimentNumber + "'" + "," + "'" + date + "'" + "," + "'" + timestampStart
				+ "'" + "," + "'" + timestampEnd + "'" + "," + "'" + engine + "'" + "," + "'" + type + "'" + "," + "'" + timecontrol + "'" + ","
				+ "'" + inputSource + "'" + "," + "'" + outputPath + "/" + getName() + "')";
		log.info("Insert Experiment [" + sql + "] in [" + where + "]");
		return SQLListeService.write(SQLListeService.BASELINE_INSERT + sql);
	}

	public String getName() {
		return "EN" + experimentNumber + "EXE" + executionNumber + "_" + date + "_" + metadata;
	}
}
