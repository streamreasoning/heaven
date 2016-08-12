package it.polimi.heaven.lubm;

import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;

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
