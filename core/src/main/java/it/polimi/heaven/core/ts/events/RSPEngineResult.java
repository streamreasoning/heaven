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

	public RSPEngineResult(String id, String streamName, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber, long outputTimestamp,
			boolean abox) {
		super(id, streamName, hashSet, eventNumber, experimentNumber, 0);
		this.outputTimestamp = outputTimestamp;
		this.abox = abox;
	}

}
