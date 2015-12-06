package it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles;

import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMFlowRateProfiler;

/**
 * 
 * x is width y is factor
 * 
 * @author Riccardo
 * 
 */
public class StepFactorFlowRateProfiler extends LUBMFlowRateProfiler {

	public StepFactorFlowRateProfiler(ParsingTemplate parser, int width, int factor, int initSize, int experiment, int timing) {
		super(FlowRateProfile.STEP, parser, width, factor, initSize, experiment, timing);
	}

	int distance = 0;

	@Override
	public void updateSize() {
		if (current_heaven_input % x == 0) {
			roundSize *= y;
		}
	}
}
