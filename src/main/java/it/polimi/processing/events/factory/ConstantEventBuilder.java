package it.polimi.processing.events.factory;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

public class ConstantEventBuilder extends RSPEventBuilder {

	public ConstantEventBuilder(int initSize, int experiment) {
		super(EventBuilderMode.CONSTANT, 0, 0, initSize, experiment);
		roundSize = initSize;
	}

	@Override
	public void updateSize() {
	}
}
