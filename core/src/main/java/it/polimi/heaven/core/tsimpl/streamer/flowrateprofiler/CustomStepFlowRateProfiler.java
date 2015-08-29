package it.polimi.heaven.core.tsimpl.streamer.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

public class CustomStepFlowRateProfiler extends TripleSetFlowRateProfiler {

	public CustomStepFlowRateProfiler(int width, int finalSize, int initSize, int experiment) {
		super(FlowRateProfile.STEP, width, finalSize, initSize, experiment);
	}

	@Override
	public void updateSize() {
		if (eventNumber == x) {
			roundSize = y;
		}
	}
}
