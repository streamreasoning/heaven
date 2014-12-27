package it.polimi.processing.events;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

import java.util.Random;

import lombok.extern.log4j.Log4j;

@Log4j
public class RandomEventBuilder extends RSPEventBuilder {

	private final Random yRandom = new Random();

	public RandomEventBuilder(int yMax, int initSize, int experiment) {
		super(EventBuilderMode.RANDOM, -1, yMax, initSize, experiment);
		roundSize = initSize;
	}

	@Override
	public void updateSize() {
		roundSize = yRandom.nextInt(y);
		log.debug("Size updated new RoundSize [" + roundSize + "]");
	}

}
