package it.polimi.processing.events.factory;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.RSPEventBuilder;

import java.util.Random;

import lombok.extern.log4j.Log4j;

@Log4j
public class ConstantRandomEventBuilder extends RSPEventBuilder {

	private final Random yRandom;
	private final Random xRandom;
	private int roundX;
	private int counter;

	public ConstantRandomEventBuilder(long xSeed, long ySeed, int xMax, int yMax, int initSize, int experiment) {
		super(EventBuilderMode.RANDOM, xMax, yMax, initSize, experiment);
		this.roundSize = initSize;
		this.roundX = xMax;
		this.counter = xMax - 1;
		this.xRandom = new Random(xSeed);
		this.yRandom = new Random(ySeed);
	}

	public ConstantRandomEventBuilder(int xMax, int yMax, int initSize, int experiment) {
		super(EventBuilderMode.RANDOM, xMax, yMax, initSize, experiment);
		this.roundSize = initSize;
		this.roundX = xMax;
		this.counter = xMax - 1;
		this.xRandom = new Random(1L);
		this.yRandom = new Random(1L);
	}

	@Override
	public void updateSize() {
		if (counter == 0) {
			do {
				this.roundX = xRandom.nextInt(x);
			} while (this.roundX == 0);
			roundSize = yRandom.nextInt(y);
			log.debug("Size updated new RoundSize [" + roundSize + "] for [" + roundX + "]");
			counter = roundX - 1;
		} else {
			counter--;
		}
	}
}
