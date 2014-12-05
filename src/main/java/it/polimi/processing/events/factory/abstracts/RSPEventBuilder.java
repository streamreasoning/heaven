package it.polimi.processing.events.factory.abstracts;

import it.polimi.processing.enums.BuildingStrategy;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class RSPEventBuilder implements EventBuilder<RSPEvent> {

	protected RSPEvent e;
	protected BuildingStrategy mode;
	protected int initSize, roundSize, eventNumber;
	protected int x, y;
	protected boolean isFull;

	public RSPEventBuilder(BuildingStrategy mode, int x, int y, int initSize) {
		this.x = x;
		this.y = y;
		this.initSize = roundSize = initSize;
		this.eventNumber = 0;
		this.mode = mode;
		this.isFull = true;
	}

	@Override
	public RSPEvent getEvent() {
		return e;
	}

	@Override
	public boolean canSend() {
		return (e != null) && isFull;
	}

	@Override
	public boolean append(Set<TripleContainer> triples, int eventNumber, int experimentNumber) {
		String id = "<http://example.org/" + experimentNumber + "/";
		if (triples.size() > roundSize) {
			throw new IllegalArgumentException("Event Size [" + triples.size() + "] out of range [" + roundSize + "]");
		} else if (isFull) {
			e = (e != null) ? e.rebuild(id, triples, eventNumber, experimentNumber) : new RSPEvent(id, triples, eventNumber, experimentNumber);
			isFull = (e.size() == roundSize);
			updateSize();
		} else {
			Set<TripleContainer> eventTriples = e.getEventTriples();
			for (TripleContainer tripleContainer : triples) {
				if (eventTriples.size() > roundSize) {
					throw new IllegalArgumentException("Event Size [" + triples.size() + "][" + e.size() + "] out of range [" + roundSize + "]");
				}
				eventTriples.add(tripleContainer);
			}
			e.setEventTriples(eventTriples);
			isFull = (e.size() == roundSize);
		}
		return isFull;
	}

	public abstract void updateSize();
}
