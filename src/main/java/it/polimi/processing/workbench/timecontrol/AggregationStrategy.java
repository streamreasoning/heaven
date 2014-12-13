package it.polimi.processing.workbench.timecontrol;

import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TSResult;
import it.polimi.processing.rspengine.windowed.RSPEngine;
import it.polimi.processing.system.GetPropertyValues;
import it.polimi.processing.system.Memory;
import lombok.extern.log4j.Log4j;

@Log4j
public class AggregationStrategy implements TimeStrategy {

	private int numberEvents = 0;
	private final int aggregation;
	int i;
	private RSPEngine rspEngine;
	private TSResult currentResult;
	private final int experiment;

	public AggregationStrategy(int aggreation) {
		this.aggregation = aggreation;
		this.experiment = GetPropertyValues.getIntegerProperty("experiment_number");
		this.i = 0;
	}

	@Override
	public boolean apply(RSPEvent e) {

		boolean process = false;
		double memoryUsage = Memory.getMemoryUsage();
		long currentTimeMillis = System.currentTimeMillis();
		if (i == 0) {

			log.debug("Memory Before Sending [" + memoryUsage + "] On Event " + numberEvents);
			currentResult = new TSResult();
			currentResult.setMemoryB(memoryUsage);
			currentResult.setInputTimestamp(currentTimeMillis);

			process = rspEngine.process(e);

			i++;
		} else if (i == aggregation - 1) {
			log.debug("Memory After Sending [" + memoryUsage + "] On Event " + numberEvents);

			int eventNumber = rspEngine.getEventNumber();
			String id = "<http://example.org/" + experiment + "/" + eventNumber + "/";
			currentResult.setId(id);
			currentResult.setEventNumber(eventNumber);
			currentResult.setMemoryA(memoryUsage);
			currentResult.setOutputTimestamp(currentTimeMillis);
			process = rspEngine.process(e);
			i = 0;
		} else {
			process = rspEngine.process(e);
			i++;
		}

		numberEvents++;
		rspEngine.progress(aggregation);
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
