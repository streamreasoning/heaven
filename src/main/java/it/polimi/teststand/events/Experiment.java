package it.polimi.teststand.events;

import it.polimi.events.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Experiment extends Event {

	private String inputFileName, outputFileName;
	private Long timestamp;

	public Experiment(String name, String inputFileName, String outputFileName) {
		setName(name);
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.timestamp = System.currentTimeMillis();
	}
}
