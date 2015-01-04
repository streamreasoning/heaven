package it.polimi.processing.events.results;

import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TripleContainer;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class RSPTripleSetResult extends RSPTripleSet {

	private long outputTimestamp;
	private boolean abox;

	public RSPTripleSetResult(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber, long outputTimestamp, boolean abox) {
		super(id, hashSet, eventNumber, experimentNumber);
		this.outputTimestamp = outputTimestamp;
		this.abox = abox;
	}

}
