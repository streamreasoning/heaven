package it.polimi.processing.events.factory;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

public class LinearEventBuilder extends RSPEventBuilder {

	public LinearEventBuilder(int height, int initSize, int experiment) {
		super(EventBuilderMode.LINEAR, height, 1, initSize, experiment);
		eventNumber = initSize;
	}

	@Override
	public void updateSize() {
		roundSize = roundSize + x;
	}
}
