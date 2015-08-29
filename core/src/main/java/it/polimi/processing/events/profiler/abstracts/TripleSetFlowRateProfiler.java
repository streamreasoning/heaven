package it.polimi.processing.events.profiler.abstracts;

import it.polimi.processing.enums.FlowRateProfile;
import it.polimi.processing.events.CTEvent;
import it.polimi.processing.events.TripleContainer;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Setter
@Getter
@Log4j
public abstract class TripleSetFlowRateProfiler implements FlowRateProfiler<CTEvent> {

	protected CTEvent e;
	protected FlowRateProfile mode;
	protected int initSize, roundSize, eventNumber;
	protected int x, y;
	protected boolean sizeReached;
	protected int experimentNumber;
	private String id;

	public TripleSetFlowRateProfiler(FlowRateProfile mode, int x, int y, int initSize, int experimentNumber) {
		this.x = x;
		this.y = y;
		this.initSize = roundSize = initSize;
		this.eventNumber = 1;
		this.mode = mode;
		this.sizeReached = false;
		id = "<http://example.org/" + experimentNumber + "/";
		e = new CTEvent(id, new HashSet<TripleContainer>(), eventNumber, experimentNumber);
	}

	@Override
	public CTEvent getEvent() {
		return sizeReached ? e : null;
	}

	@Override
	public boolean isReady() {
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
