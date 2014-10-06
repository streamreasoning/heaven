package it.polimi.teststand.events;


public class Experiment extends Event {

	private String inputFileName, outputFileName, name;
	private Long timestamp;

	public Experiment(String name, String inputFileName, String outputFileName) {
		this.name = name;
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.timestamp = System.currentTimeMillis();
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public String getInputFileName() {
		return inputFileName;
	}

	public String getName() {
		return name;
	}

}
