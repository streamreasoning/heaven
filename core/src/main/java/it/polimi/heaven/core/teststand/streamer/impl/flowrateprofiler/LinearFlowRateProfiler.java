package it.polimi.heaven.core.teststand.streamer.impl.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

public class LinearFlowRateProfiler extends TripleSetFlowRateProfiler {

	public LinearFlowRateProfiler(int height, int initSize, int experiment, int timing) {
		super(FlowRateProfile.LINEAR, height, 1, initSize, experiment, timing);
		eventNumber = initSize;
	}

	@Override
	public void updateSize() {
		roundSize = roundSize + x;
	}
}
