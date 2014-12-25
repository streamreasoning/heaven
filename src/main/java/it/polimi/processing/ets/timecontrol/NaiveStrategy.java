package it.polimi.processing.ets.timecontrol;

import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.results.TSResult;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.services.system.GetPropertyValues;
import it.polimi.processing.services.system.Memory;

public class NaiveStrategy implements TimeStrategy {

	private RSPEngine rspEngine;
	private TSResult currentResult;
	private final int experiment;

	public NaiveStrategy() {
		this.experiment = GetPropertyValues.getIntegerProperty("experiment_number");
	}

	@Override
	public boolean apply(RSPTripleSet e) {
		int eventNumber = rspEngine.getEventNumber();
		String id = "<http://example.org/" + experiment + "/" + eventNumber + ">";

		currentResult = new TSResult();
		currentResult.setMemoryB(Memory.getMemoryUsage());
		currentResult.setInputTimestamp(System.currentTimeMillis());
		currentResult.setId(id);
		currentResult.setEventNumber(eventNumber);

		boolean process = rspEngine.process(e);
		rspEngine.timeProgress();
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
