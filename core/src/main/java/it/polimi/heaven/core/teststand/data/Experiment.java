package it.polimi.heaven.core.teststand.data;

import lombok.Data;

@Data
public class Experiment {

	private String date;
	private int experimentNumber;
	private int executionNumber;
	private String metadata;

	private String engine;
	private String timecontrol;

	private boolean memoryLog;
	private boolean latencyLog;
	private boolean resultLog;
	private boolean aboxLog;

	private String inputSource;
	private String outputPath;

	private long responsivity;

	public String getName() {
		return "EN" + experimentNumber + "EXE" + executionNumber + "_" + date + "_" + metadata;
	}
}
