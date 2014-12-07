package it.polimi.processing.events.factory;

import it.polimi.processing.enums.BuildingStrategy;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

public class StepEventBuilder extends RSPEventBuilder {

	public StepEventBuilder(int height, int width, int initSize) {
		super(BuildingStrategy.STEP, height, width, initSize);
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