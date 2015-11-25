package it.polimi.heaven.core.tsimpl.streamer.rdf2rdfstream.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

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
