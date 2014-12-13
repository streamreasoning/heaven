package it.polimi.processing.workbench.timecontrol;

import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TSResult;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.system.GetPropertyValues;
import it.polimi.processing.system.Memory;
import lombok.extern.log4j.Log4j;

@Log4j
public class AggregationStrategy implements TimeStrategy {

	private int tsEventNumber;
	private final int aggregation;
	private RSPEngine rspEngine;
	private TSResult currentResult;
	private final int experiment;
	private final int tsEventMax;

	public AggregationStrategy() {
		this.aggregation = GetPropertyValues.getIntegerProperty("aggregation");
		this.experiment = GetPropertyValues.getIntegerProperty("experiment_number");
		this.tsEventNumber = 0;
		this.tsEventMax = aggregation - 1;
	}

	@Override
	public boolean apply(RSPEvent e) {

		boolean process = false;
		int eventNumber = rspEngine.getEventNumber();

		if (tsEventNumber == 0) {
			currentResult = new TSResult(System.currentTimeMillis(), Memory.getMemoryUsage());
			tsEventNumber++;
		} else if (tsEventNumber == tsEventMax) {
			tsEventNumber = 0;
		} else {
			currentResult.setInputTimestamp(System.currentTimeMillis());
			currentResult.setMemoryB(Memory.getMemoryUsage());
			tsEventNumber++;
		}

		currentResult.setEventNumber(eventNumber);
		currentResult.setId("<http://example.org/" + experiment + "/" + eventNumber + ">");
		process = rspEngine.process(e);
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
