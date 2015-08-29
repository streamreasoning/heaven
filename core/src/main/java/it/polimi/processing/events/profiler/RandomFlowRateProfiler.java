package it.polimi.processing.events.profiler;

import it.polimi.processing.enums.FlowRateProfile;
import it.polimi.processing.events.profiler.abstracts.TripleSetFlowRateProfiler;

import java.util.Random;

import lombok.extern.log4j.Log4j;

@Log4j
public class RandomFlowRateProfiler extends TripleSetFlowRateProfiler {

	private final Random yRandom;

	public RandomFlowRateProfiler(long seed, int yMax, int initSize, int experiment) {
		super(FlowRateProfile.RANDOM, -1, yMax, initSize, experiment);
		roundSize = initSize;
		this.yRandom = new Random(seed);
	}

	public RandomFlowRateProfiler(int yMax, int initSize, int experiment) {
		super(FlowRateProfile.RANDOM, -1, yMax, initSize, experiment);
		roundSize = initSize;
		this.yRandom = new Random(1L);
	}

	@Override
	public void updateSize() {
		roundSize = yRandom.nextInt(y);
		log.debug("Size updated new RoundSize [" + roundSize + "]");
	}

}
