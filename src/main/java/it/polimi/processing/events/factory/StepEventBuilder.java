package it.polimi.processing.events.factory;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

public class StepEventBuilder extends RSPEventBuilder {

	public StepEventBuilder(int width, int height, int initSize, int experiment) {
		super(EventBuilderMode.STEP, width, height, initSize, experiment);
		eventNumber = initSize;
	}

	int distance = 0;

	@Override
	public void updateSize() {
		if (eventNumber % x == 0) {
			roundSize += y;
		}
	}
}
