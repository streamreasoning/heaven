package it.polimi.processing.events.factory;

import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.factory.abstracts.TripleSetFlowRateProfiler;

public class ExpEventFlowRateProfiler extends TripleSetFlowRateProfiler {

	private int power = 0;

	public ExpEventFlowRateProfiler(int base, int initSize, int experiment) {
		super(EventBuilderMode.EXP, base, 1, initSize, experiment);
		eventNumber = initSize;
	}

	@Override
	public void updateSize() {
		power++;
		roundSize = (int) Math.pow(x, power);
	}
}
