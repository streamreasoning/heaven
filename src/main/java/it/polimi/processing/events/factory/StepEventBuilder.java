package it.polimi.processing.events.factory;

import it.polimi.processing.enums.BuildingStrategy;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

import java.util.Set;

public class StepEventBuilder extends RSPEventBuilder {

	public StepEventBuilder(int height, int width, int initSize) {
		super(BuildingStrategy.STEP, height, width, initSize);
		actualSize = initSize;

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

		} else {
			Set<TripleContainer> eventTriples = e.getEventTriples();
			eventTriples.addAll(triples);
			e.setEventTriples(eventTriples);
			actualSize += triples.size();

		}
		return condition = (actualSize == size);
	}

	@Override
	public void updateSize() {
		size = size + x;
	}
}
