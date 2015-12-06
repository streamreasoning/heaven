package it.polimi.heaven.core.ts.streamer.impl.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

public class CustomStepFlowRateProfiler extends TripleSetFlowRateProfiler {

	public CustomStepFlowRateProfiler(int width, int finalSize, int initSize, int experiment, int timing) {
		super(FlowRateProfile.STEP, width, finalSize, initSize, experiment, timing);
	}

	@Override
	public void updateSize() {
		if (eventNumber == x) {
			roundSize = y;
		}
	}
}
