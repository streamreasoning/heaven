package it.polimi.processing.events.profiler.abstracts;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TripleContainer;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Setter
@Getter
@Log4j
public abstract class TripleSetFlowRateProfiler implements FlowRateProfiler<RSPTripleSet> {

	protected RSPTripleSet e;
	protected EventBuilderMode mode;
	protected int initSize, roundSize, eventNumber;
	protected int x, y;
	protected boolean sizeReached;
	protected int experimentNumber;
	private String id;

	public TripleSetFlowRateProfiler(EventBuilderMode mode, int x, int y, int initSize, int experimentNumber) {
		this.x = x;
		this.y = y;
		this.initSize = roundSize = initSize;
		this.eventNumber = 1;
		this.mode = mode;
		this.sizeReached = false;
		id = "<http://example.org/" + experimentNumber + "/";
		e = new RSPTripleSet(id, new HashSet<TripleContainer>(), eventNumber, experimentNumber);
	}

	@Override
	public RSPTripleSet getEvent() {
		return sizeReached ? e : null;
	}

	@Override
	public boolean canSend() {
		return sizeReached;
	}

	@Override
	public boolean append(TripleContainer triple) {
		if (sizeReached) {
			updateSize();
			Set<TripleContainer> set = new HashSet<TripleContainer>();
			if (roundSize > 0) {
				eventNumber++;
				set.add(triple);
			}
			e = e.rebuild(id, set, eventNumber, experimentNumber);
			log.debug("is Full Event Size [" + e.size() + "] roundSize [" + roundSize + "]");
		} else {
			e.getEventTriples().add(triple);
			log.debug("NotFull Event Size [" + e.size() + "] roundSize [" + roundSize + "]");
		}

		sizeReached = (e.size() == roundSize);

		return sizeReached;
	}

	public abstract void updateSize();
}
