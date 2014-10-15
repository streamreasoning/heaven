package it.polimi.processing.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Experiment extends Event{

	private String name, inputFileName, outputFileName;
	private Long timestamp;

	public Experiment(String name, String inputFileName, String outputFileName) {
		this.name = name;
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.timestamp = System.currentTimeMillis();
	}

}
