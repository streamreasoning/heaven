package it.polimi.processing.events.profiler;

import it.polimi.processing.enums.FlowRateProfile;
import it.polimi.processing.events.profiler.abstracts.TripleSetFlowRateProfiler;

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
