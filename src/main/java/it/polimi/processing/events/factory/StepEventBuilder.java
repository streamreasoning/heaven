package it.polimi.processing.events.factory;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

public class StepEventBuilder extends RSPEventBuilder {

	public StepEventBuilder(int height, int width, int initSize, int experiment) {
		super(EventBuilderMode.STEP, height, width, initSize, experiment);
		eventNumber = initSize;
	}

	@Override
	public void updateSize() {
		if (roundSize == initSize && x != initSize) {
			roundSize = x;
		} else {
			roundSize += x;
		}
	}
}
