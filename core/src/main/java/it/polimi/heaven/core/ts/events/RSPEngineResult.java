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
		// TODO 0 is the stimulus timestamp assegnated by the flow rate
		// profiler, find a proper way to use this within the context of the
		// RSPER
		super(id, hashSet, eventNumber, experimentNumber, 0);
		this.outputTimestamp = outputTimestamp;
		this.abox = abox;
	}

}
