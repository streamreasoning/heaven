package it.polimi.heaven.core.ts.streamer.impl.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

/**
 * 
 * x is width y is factor
 * 
 * @author Riccardo
 * 
 */
public class StepFactorFlowRateProfiler extends TripleSetFlowRateProfiler {

	public StepFactorFlowRateProfiler(int width, int factor, int initSize, int experiment, int timing) {
		super(FlowRateProfile.STEP, width, factor, initSize, experiment, timing);
	}

	int distance = 0;

	@Override
	public void updateSize() {
		if (eventNumber % x == 0) {
			roundSize *= y;
		}
	}
}
