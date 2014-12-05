package it.polimi.processing.events.factory;

import it.polimi.processing.enums.BuildingStrategy;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

public class LinearEventBuilder extends RSPEventBuilder {

	public LinearEventBuilder(int height, int initSize) {
		super(BuildingStrategy.LINEAR, height, 1, initSize);
		eventNumber = initSize;
	}

	@Override
	public void updateSize() {
		roundSize = roundSize + x;
	}
}
