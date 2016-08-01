package it.polimi.heaven.lubm;

import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;

public class ConstantFlowRateProfiler extends LUBMFlowRateProfiler {

	public ConstantFlowRateProfiler(ParsingTemplate parser, int initSize, int experiment, int timing) {
		super(FlowRateProfile.CONSTANT, parser, 0, 0, initSize, experiment, timing);
		roundSize = initSize;
	}

	@Override
	public void updateSize() {
	}
}
