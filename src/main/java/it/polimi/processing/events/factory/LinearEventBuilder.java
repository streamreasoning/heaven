package it.polimi.processing.events.factory;

import it.polimi.processing.enums.BuildingStrategy;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

public class LinearEventBuilder extends RSPEventBuilder {

	public LinearEventBuilder(int height, int initSize) {
		super(BuildingStrategy.LINEAR, height, 1, initSize);
		actualSize = initSize;
	}

	@Override
	public void updateSize() {
		size = size + x;
	}
}
