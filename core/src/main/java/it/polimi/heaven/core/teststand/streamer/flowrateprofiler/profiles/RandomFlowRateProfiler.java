package it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles;

import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMFlowRateProfiler;

import java.util.Random;

import lombok.extern.log4j.Log4j;

@Log4j
public class RandomFlowRateProfiler extends LUBMFlowRateProfiler {

	private final Random yRandom;

	public RandomFlowRateProfiler(ParsingTemplate parser, long seed, int yMax, int initSize, int experiment, int timing) {
		super(FlowRateProfile.RANDOM, parser, -1, yMax, initSize, experiment, timing);
		roundSize = initSize;
		this.yRandom = new Random(seed);
	}

	public RandomFlowRateProfiler(ParsingTemplate parser, int yMax, int initSize, int experiment, int timing) {
		super(FlowRateProfile.RANDOM, parser, -1, yMax, initSize, experiment, timing);
		roundSize = initSize;
		this.yRandom = new Random(1L);
	}

	@Override
	public void updateSize() {
		roundSize = yRandom.nextInt(y);
		log.debug("Size updated new RoundSize [" + roundSize + "]");
	}

}
