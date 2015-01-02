package it.polimi.processing.events.factory;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.TripleSetFlowRateProfiler;

public class LinearFlowRateProfiler extends TripleSetFlowRateProfiler {

	public LinearFlowRateProfiler(int height, int initSize, int experiment) {
		super(EventBuilderMode.LINEAR, height, 1, initSize, experiment);
		eventNumber = initSize;
	}

	@Override
	public void updateSize() {
		roundSize = roundSize + x;
	}
}
