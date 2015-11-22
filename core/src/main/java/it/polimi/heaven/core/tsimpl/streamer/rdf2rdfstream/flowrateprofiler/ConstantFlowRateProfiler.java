package it.polimi.heaven.core.tsimpl.streamer.rdf2rdfstream.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

public class ConstantFlowRateProfiler extends TripleSetFlowRateProfiler {

	public ConstantFlowRateProfiler(int initSize, int experiment) {
		super(FlowRateProfile.CONSTANT, 0, 0, initSize, experiment);
		roundSize = initSize;
	}

	@Override
	public void updateSize() {
	}
}
