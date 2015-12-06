package it.polimi.heaven.core.ts.streamer.impl.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

public class ConstantFlowRateProfiler extends TripleSetFlowRateProfiler {

	public ConstantFlowRateProfiler(int initSize, int experiment, int timing) {
		super(FlowRateProfile.CONSTANT, 0, 0, initSize, experiment, timing);
		roundSize = initSize;
	}

	@Override
	public void updateSize() {
	}
}
