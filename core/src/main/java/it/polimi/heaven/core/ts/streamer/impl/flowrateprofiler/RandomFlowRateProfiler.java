package it.polimi.heaven.core.ts.streamer.impl.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

import java.util.Random;

import lombok.extern.log4j.Log4j;

@Log4j
public class RandomFlowRateProfiler extends TripleSetFlowRateProfiler {

	private final Random yRandom;

	public RandomFlowRateProfiler(long seed, int yMax, int initSize, int experiment, int timing) {
		super(FlowRateProfile.RANDOM, -1, yMax, initSize, experiment, timing);
		roundSize = initSize;
		this.yRandom = new Random(seed);
	}

	public RandomFlowRateProfiler(int yMax, int initSize, int experiment, int timing) {
		super(FlowRateProfile.RANDOM, -1, yMax, initSize, experiment, timing);
		roundSize = initSize;
		this.yRandom = new Random(1L);
	}

	@Override
	public void updateSize() {
		roundSize = yRandom.nextInt(y);
		log.debug("Size updated new RoundSize [" + roundSize + "]");
	}

}
