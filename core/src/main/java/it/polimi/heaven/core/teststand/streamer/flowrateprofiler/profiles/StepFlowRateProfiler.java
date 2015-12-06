package it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles;

import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMFlowRateProfiler;

public class StepFlowRateProfiler extends LUBMFlowRateProfiler {

	public StepFlowRateProfiler(ParsingTemplate parser, int width, int height, int initSize, int experiment, int timing) {
		super(FlowRateProfile.STEP, parser, width, height, initSize, experiment, timing);
	}

	int distance = 0;

	@Override
	public void updateSize() {
		if (current_heaven_input % x == 0) {
			roundSize += y;
		}
	}
}
