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
	protected int initSize, size, actualSize;
	protected int x, y;
	protected boolean condition;

	public RSPEventBuilder(BuildingStrategy mode, int x, int y, int initSize) {
		this.x = x;
		this.y = y;
		this.initSize = size = initSize;
		this.actualSize = 0;
		this.mode = BuildingStrategy.STEP;
		e = new RSPEvent();
	}

	@Override
	public RSPEvent getEvent() {
		return e;
	}

	@Override
	public boolean canSend() {
		return condition;
	}

	@Override
	public boolean append(Set<TripleContainer> triples, int eventNumber, int experimentNumber) {
		if (condition) {
			e.reset("<http://example.org/" + experimentNumber + "/", triples, eventNumber, experimentNumber);
			actualSize = 1;
			updateSize();
		} else {
			Set<TripleContainer> eventTriples = e.getEventTriples();
			eventTriples.addAll(triples);
			e.setEventTriples(eventTriples);
			actualSize += triples.size();

		}
		return condition = (actualSize == size);
	}

	public abstract void updateSize();
}
