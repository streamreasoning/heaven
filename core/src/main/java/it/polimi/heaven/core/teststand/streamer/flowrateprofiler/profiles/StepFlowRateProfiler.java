package it.polimi.heaven.core.teststand.streamer.impl.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

public class StepFlowRateProfiler extends TripleSetFlowRateProfiler {

	public StepFlowRateProfiler(int width, int height, int initSize, int experiment, int timing) {
		super(FlowRateProfile.STEP, width, height, initSize, experiment, timing);
	}

	int distance = 0;

	@Override
	public void updateSize() {
		if (eventNumber % x == 0) {
			roundSize += y;
		}
	}
}
