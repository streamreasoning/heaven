package it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles;

import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMFlowRateProfiler;

public class LinearFlowRateProfiler extends LUBMFlowRateProfiler {

	public LinearFlowRateProfiler(ParsingTemplate parser, int height, int initSize, int experiment, int timing) {
		super(FlowRateProfile.LINEAR, parser, height, 1, initSize, experiment, timing);
		current_heaven_input = initSize;
	}

	@Override
	public void updateSize() {
		roundSize = roundSize + x;
	}
}
