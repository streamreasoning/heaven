package it.polimi.export.processing.events;

import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.results.OutCTEvent;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class CSResult extends OutCTEvent {

	private Boolean completeSMPL, soundSMPL, completeRHODF, soundRHODF;

	public CSResult(String id, Set<TripleContainer> statements, int eventNumber, long timestamp, Boolean abox, Boolean completeSMPL,
			Boolean soundSMPL, Boolean completeRHODF, Boolean soundRHODF) {
		super(id, statements, eventNumber, 0, timestamp, abox);
		this.completeRHODF = completeRHODF;
		this.soundRHODF = soundRHODF;
		this.completeSMPL = completeSMPL;
		this.soundSMPL = soundSMPL;
	}

}
