package it.polimi.processing.events.factory;

import it.polimi.processing.enums.BuildingStrategy;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

public class ConstantEventBuilder extends RSPEventBuilder {

	public ConstantEventBuilder(int initSize) {
		super(BuildingStrategy.COSTANT, 0, 0, initSize);
		roundSize = initSize;
	}

	@Override
	public void updateSize() {
	}
}
