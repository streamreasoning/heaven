package it.polimi.processing.events.profiler;

import it.polimi.processing.enums.FlowRateProfile;
import it.polimi.processing.events.profiler.abstracts.TripleSetFlowRateProfiler;

/**
 * 
 * x is width
 * y is factor
 * 
 * @author Riccardo
 * 
 */
public class StepFactorFlowRateProfiler extends TripleSetFlowRateProfiler {

	public StepFactorFlowRateProfiler(int width, int factor, int initSize, int experiment) {
		super(FlowRateProfile.STEP, width, factor, initSize, experiment);
	}

	int distance = 0;

	@Override
	public void updateSize() {
		if (eventNumber % x == 0) {
			roundSize *= y;
		}
	}
}
