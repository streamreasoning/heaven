package it.polimi.processing.events.factory;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

import java.util.Random;

import lombok.extern.log4j.Log4j;

@Log4j
public class RandomEventBuilder extends RSPEventBuilder {

	private final Random yRandom;

	public RandomEventBuilder(long seed, int yMax, int initSize, int experiment) {
		super(EventBuilderMode.RANDOM, -1, yMax, initSize, experiment);
		roundSize = initSize;
		this.yRandom = new Random(seed);
	}

	public RandomEventBuilder(int yMax, int initSize, int experiment) {
		super(EventBuilderMode.RANDOM, -1, yMax, initSize, experiment);
		roundSize = initSize;
		this.yRandom = new Random(1L);
	}

	@Override
	public void updateSize() {
		roundSize = yRandom.nextInt(y);
		log.debug("Size updated new RoundSize [" + roundSize + "]");
	}

}
