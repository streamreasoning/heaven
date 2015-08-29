package it.polimi.heaven.core.ts.events;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class RSPEngineResult extends Stimulus {

	private long outputTimestamp;
	private boolean abox;

	public RSPEngineResult(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber, long outputTimestamp, boolean abox) {
		super(id, hashSet, eventNumber, experimentNumber);
		this.outputTimestamp = outputTimestamp;
		this.abox = abox;
	}

}
