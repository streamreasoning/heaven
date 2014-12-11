package it.polimi.processing.events.factory.abstracts;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Setter
@Getter
@Log4j
public abstract class RSPEventBuilder implements EventBuilder<RSPEvent> {

	protected RSPEvent e;
	protected EventBuilderMode mode;
	protected int initSize, roundSize, eventNumber;
	protected int x, y;
	protected boolean sizeReached;
	protected int experimentNumber;

	public RSPEventBuilder(EventBuilderMode mode, int x, int y, int initSize, int experimentNumber) {
		this.x = x;
		this.y = y;
		this.initSize = roundSize = initSize;
		this.eventNumber = 1;
		this.mode = mode;
		this.sizeReached = true;
	}

	@Override
	public RSPEvent getEvent() {
		return sizeReached ? e : null;
	}

	@Override
	public boolean canSend() {
		return (e != null) && sizeReached;
	}

	@Override
	public boolean append(Set<TripleContainer> triples) {
		if (triples.size() > roundSize) {
			throw new IllegalArgumentException("Event Size [" + triples.size() + "] out of range [" + roundSize + "]");
		} else if (sizeReached || e == null) {
			String id = "<http://example.org/" + experimentNumber + "/";
			e = (e != null) ? e.rebuild(id, triples, eventNumber, experimentNumber) : new RSPEvent(id, triples, eventNumber, experimentNumber);
			sizeReached = (e.size() == roundSize);
			eventNumber++;
			updateSize();
		} else {
			Set<TripleContainer> eventTriples = e.getEventTriples();
			for (TripleContainer tripleContainer : triples) {
				if (eventTriples.size() > roundSize) {
					throw new IllegalArgumentException("Event Size [" + triples.size() + "][" + e.size() + "] out of range [" + roundSize + "]");
				}
				eventTriples.add(tripleContainer);
			}
			sizeReached = (e.size() == roundSize);

		}
		return sizeReached;
	}

	@Override
	public boolean append(TripleContainer triple) {
		Set<TripleContainer> set = new HashSet<TripleContainer>();
		String id = "<http://example.org/" + experimentNumber + "/";
		if (e == null) {
			set = new HashSet<TripleContainer>();
			set.add(triple);
			e = new RSPEvent(id, set, eventNumber, experimentNumber);
			log.debug("isNull Event Size [" + e.size() + "] roundSize [" + roundSize + "]");
		} else if (sizeReached) {
			eventNumber++;
			updateSize();
			set.add(triple);
			e = e.rebuild(id, set, eventNumber, experimentNumber);
			log.debug("isFull Event Size [" + e.size() + "] roundSize [" + roundSize + "]");
		} else if (!(e.size() >= roundSize)) {
			e.getEventTriples().add(triple);
			log.debug("NotFull Event Size [" + e.size() + "] roundSize [" + roundSize + "]");
		}

		sizeReached = (e.size() == roundSize);
		return sizeReached;
	}

	public abstract void updateSize();
}
