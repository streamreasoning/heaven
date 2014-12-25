package it.polimi.processing.events.results;

import it.polimi.processing.events.TripleContainer;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class CSResult extends Result {

	private Boolean completeSMPL, soundSMPL, completeRHODF, soundRHODF;

	public CSResult(String id, Set<TripleContainer> statements, int eventNumber, long timestamp, Boolean abox, Boolean completeSMPL,
			Boolean soundSMPL, Boolean completeRHODF, Boolean soundRHODF) {
		super(id, statements, eventNumber, timestamp, abox);
		this.completeRHODF = completeRHODF;
		this.soundRHODF = soundRHODF;
		this.completeSMPL = completeSMPL;
		this.soundSMPL = soundSMPL;
	}

}
