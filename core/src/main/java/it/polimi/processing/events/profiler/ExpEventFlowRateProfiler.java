package it.polimi.processing.events.profiler;

import it.polimi.processing.enums.FlowRateProfile;
import it.polimi.processing.events.profiler.abstracts.TripleSetFlowRateProfiler;

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
