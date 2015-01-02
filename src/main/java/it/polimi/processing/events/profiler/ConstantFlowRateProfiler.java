package it.polimi.processing.events.profiler;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.profiler.abstracts.TripleSetFlowRateProfiler;

public class ConstantFlowRateProfiler extends TripleSetFlowRateProfiler {

	public ConstantFlowRateProfiler(int initSize, int experiment) {
		super(EventBuilderMode.CONSTANT, 0, 0, initSize, experiment);
		roundSize = initSize;
	}

	@Override
	public void updateSize() {
	}
}
