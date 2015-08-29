package it.polimi.heaven.core.tsimpl.strategic.timecontrol;

import it.polimi.heaven.core.ts.events.HeavenResult;
import it.polimi.heaven.core.ts.events.Stimulus;
import it.polimi.heaven.core.ts.rspengine.RSPEngine;
import it.polimi.heaven.services.system.Memory;
import it.polimi.utils.GetPropertyValues;

public class NaiveStrategy implements TimeStrategy {

	private RSPEngine rspEngine;
	private HeavenResult currentResult;
	private final int experiment;

	public NaiveStrategy() {
		this.experiment = GetPropertyValues.getIntegerProperty("experiment_number");
	}

	@Override
	public boolean apply(Stimulus e) {
		int eventNumber = rspEngine.getEventNumber();
		String id = "<http://example.org/" + experiment + "/" + eventNumber + ">";

		currentResult = new HeavenResult();
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
	public HeavenResult getResult() {
		return currentResult;
	}
}
