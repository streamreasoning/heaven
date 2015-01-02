package it.polimi.processing.events.profiler;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.profiler.abstracts.TripleSetFlowRateProfiler;

public class StepFlowRateProfiler extends TripleSetFlowRateProfiler {

	public StepFlowRateProfiler(int width, int height, int initSize, int experiment) {
		super(EventBuilderMode.STEP, width, height, initSize, experiment);
		eventNumber = initSize;
	}

	int distance = 0;

	@Override
	public void updateSize() {
		if (eventNumber % x == 0) {
			roundSize += y;
		}
	}
}
