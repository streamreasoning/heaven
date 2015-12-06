package it.polimi.heaven.core.teststand.streamer.impl.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

public class ConstantFlowRateProfiler extends TripleSetFlowRateProfiler {

	public ConstantFlowRateProfiler(int initSize, int experiment, int timing) {
		super(FlowRateProfile.CONSTANT, 0, 0, initSize, experiment, timing);
		roundSize = initSize;
	}

	@Override
	public void updateSize() {
	}
}
