package it.polimi.processing.events.factory;

import it.polimi.processing.enums.BuildingStrategy;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

import java.util.Set;

public class StepEventBuilder extends RSPEventBuilder {

	public StepEventBuilder(int height, int width, int initSize) {
		super(BuildingStrategy.STEP, height, width, initSize);
		eventNumber = initSize;

	}

	@Override
	public RSPEvent getEvent() {
		return e;
	}

	@Override
	public boolean canSend() {
		return isFull;
	}

	@Override
	public boolean append(Set<TripleContainer> triples, int eventNumber, int experimentNumber) {
		if (isFull) {
			e.rebuild("<http://example.org/" + experimentNumber + "/", triples, eventNumber, experimentNumber);
			eventNumber = 1;
		} else {
			Set<TripleContainer> eventTriples = e.getEventTriples();
			eventTriples.addAll(triples);
			e.setEventTriples(eventTriples);
			eventNumber += triples.size();

		}
		return isFull = (eventNumber == roundSize);
	}

	@Override
	public void updateSize() {
		roundSize = roundSize + x;
	}
}
