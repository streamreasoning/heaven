package it.polimi.processing.events;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class StreamingEvent extends Event {

	private Set<String[]> eventTriples;
	private String id;
	private int eventNumber, experimentNumber;
	private int tripleGraph;
	private int[] lineNumbers;
	private long timestamp;

}
