package it.polimi.processing.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Experiment extends Event {
	private int experimentNumber;
	private String descr, engine, inputFileName, outputFileName;
	private Long timestamp;

}
