package it.polimi.processing.workbench.timecontrol;

import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TSResult;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.rspengine.shared.events.EsperUtils;
import it.polimi.processing.system.GetPropertyValues;
import it.polimi.processing.system.Memory;

public class NoAggregationStrategy implements TimeStrategy {

	private RSPEngine rspEngine;
	private TSResult currentResult;
	private final int experiment;

	public NoAggregationStrategy() {
		this.experiment = GetPropertyValues.getIntegerProperty("experiment_number");
	}

	@Override
	public boolean apply(RSPTripleSet e) {
		int eventNumber = rspEngine.getEventNumber();
		String id = "<http://example.org/" + experiment + "/" + eventNumber + "/";

		currentResult = new TSResult();
		currentResult.setMemoryB(Memory.getMemoryUsage());
		currentResult.setInputTimestamp(System.currentTimeMillis());
		currentResult.setId(id);
		currentResult.setEventNumber(eventNumber);
		currentResult.setOutputTimestamp(System.currentTimeMillis());
		currentResult.setMemoryA(Memory.getMemoryUsage());

		boolean process = rspEngine.process(e);
		rspEngine.progress(EsperUtils.WINDOW_SIZE / EsperUtils.OUTPUT_RATE);
		return process;
	}

	@Override
	public void setRSPEngine(RSPEngine rspEngine) {
		this.rspEngine = rspEngine;
	}

	@Override
	public TSResult getResult() {
		return currentResult;
	}
}
