package it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles;

import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMFlowRateProfiler;

public class CustomStepFlowRateProfiler extends LUBMFlowRateProfiler {

	public CustomStepFlowRateProfiler(ParsingTemplate parser, int width, int finalSize, int initSize, int experiment, int timing) {
		super(FlowRateProfile.STEP, parser, width, finalSize, initSize, experiment, timing);
	}

	@Override
	public void updateSize() {
		if (current_heaven_input == x) {
			roundSize = y;
		}
	}
}
