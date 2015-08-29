package it.polimi.processing.events.profiler;

import it.polimi.processing.enums.FlowRateProfile;
import it.polimi.processing.events.profiler.abstracts.TripleSetFlowRateProfiler;

public class LinearFlowRateProfiler extends TripleSetFlowRateProfiler {

	public LinearFlowRateProfiler(int height, int initSize, int experiment) {
		super(FlowRateProfile.LINEAR, height, 1, initSize, experiment);
		eventNumber = initSize;
	}

	@Override
	public void updateSize() {
		roundSize = roundSize + x;
	}
}
