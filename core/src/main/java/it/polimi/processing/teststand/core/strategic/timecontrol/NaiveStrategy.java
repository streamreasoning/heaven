package it.polimi.processing.teststand.core.strategic.timecontrol;

import it.polimi.processing.events.CTEvent;
import it.polimi.processing.events.results.TSResult;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.services.system.Memory;
import it.polimi.utils.GetPropertyValues;

public class NaiveStrategy implements TimeStrategy {

	private RSPEngine rspEngine;
	private TSResult currentResult;
	private final int experiment;

	public NaiveStrategy() {
		this.experiment = GetPropertyValues.getIntegerProperty("experiment_number");
	}

	@Override
	public boolean apply(CTEvent e) {
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
