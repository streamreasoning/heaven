package it.polimi.processing.events.factory;

import it.polimi.processing.enums.BuildingStrategy;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

public class ExpEventBuilder extends RSPEventBuilder {

	private int power = 0;

	public ExpEventBuilder(int base, int initSize) {
		super(BuildingStrategy.EXP, base, 1, initSize);
		eventNumber = initSize;
	}

	@Override
	public void updateSize() {
		power++;
		roundSize = (int) Math.pow(x, power);
	}
}
