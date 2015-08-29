package it.polimi.heaven.core.tsimpl.streamer.flowrateprofiler;

import it.polimi.heaven.core.enums.FlowRateProfile;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.TripleSetFlowRateProfiler;

public class ExpEventFlowRateProfiler extends TripleSetFlowRateProfiler {

	private int power = 0;

	public ExpEventFlowRateProfiler(int base, int initSize, int experiment) {
		super(FlowRateProfile.EXP, base, 1, initSize, experiment);
		eventNumber = initSize;
	}

	@Override
	public void updateSize() {
		power++;
		roundSize = (int) Math.pow(x, power);
	}
}
