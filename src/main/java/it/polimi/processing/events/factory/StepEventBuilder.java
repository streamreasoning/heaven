package it.polimi.processing.events.factory;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

public class StepEventBuilder extends RSPEventBuilder {

	public StepEventBuilder(int height, int width, int initSize, int experiment) {
		super(EventBuilderMode.STEP, height, width, initSize, experiment);
		eventNumber = initSize;
	}

	int distance = 0;

	@Override
	public void updateSize() {
		if (roundSize == initSize && y != initSize) {
			roundSize = y;
		} else if (distance == x) {
			roundSize += y;
			distance = 0;
		} else {
			distance++;
		}
	}
}
