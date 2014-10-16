package it.polimi.processing.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Experiment extends Event {

	private String name, engine, inputFileName, outputFileName, logFileName;
	private Long timestamp;

	public Experiment(String engine, String name, String inputFileName, String outputFileName,String logFileName) {
		this.name = name;
		this.engine=engine;
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.timestamp = System.currentTimeMillis();
	}

}
