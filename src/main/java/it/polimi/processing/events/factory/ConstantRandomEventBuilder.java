package it.polimi.processing.events.factory;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

import java.util.Random;

import lombok.extern.log4j.Log4j;

@Log4j
public class ConstantRandomEventBuilder extends RSPEventBuilder {

	private final Random yRandom = new Random();
	private final Random xRandom = new Random();

	public ConstantRandomEventBuilder(int xMax, int yMax, int initSize, int experiment) {
		super(EventBuilderMode.RANDOM, xMax, yMax, initSize, experiment);
		this.roundSize = initSize;
		this.roundX = xMax;
		this.counter = xMax - 1;

	}

	private int roundX;
	private int counter;

	@Override
	public void updateSize() {
		if (counter == 0) {
			do {
				this.roundX = xRandom.nextInt(x);
			} while (this.roundX == 0);
			roundSize = yRandom.nextInt(y);
			log.info("Size updated new RoundSize [" + roundSize + "] for [" + roundX + "]");
			counter = roundX - 1;
		} else {
			counter--;
		}
	}
}
