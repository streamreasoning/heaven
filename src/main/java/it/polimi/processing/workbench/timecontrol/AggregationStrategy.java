package it.polimi.processing.workbench.timecontrol;

import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TSResult;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.rspengine.shared.events.EsperUtils;
import it.polimi.processing.system.GetPropertyValues;
import it.polimi.processing.system.Memory;
import lombok.extern.log4j.Log4j;

@Log4j
public class AggregationStrategy implements TimeStrategy {

	private int tsEventNumber;
	private final int aggregation;
	private RSPEngine rspEngine;
	private final TSResult currentResult;
	private final int experiment;
	private final int tsEventMax;

	public AggregationStrategy() {
		this.aggregation = GetPropertyValues.getIntegerProperty("aggregation");
		this.experiment = GetPropertyValues.getIntegerProperty("experiment_number");
		this.tsEventNumber = 0;
		this.tsEventMax = aggregation - 1;
		currentResult = new TSResult();
	}

	@Override
	public boolean apply(RSPTripleSet e) {

		boolean process = false;
		int eventNumber = rspEngine.getEventNumber();

		int divider = EsperUtils.OUTPUT_RATE;
		if (tsEventMax == 0 && tsEventNumber == 0) {
			currentResult.setInputTimestamp(System.currentTimeMillis());
			currentResult.setMemoryB(Memory.getMemoryUsage());
			currentResult.setEventNumber(eventNumber / aggregation);
			currentResult.setId("<http://example.org/" + experiment + "/" + eventNumber / aggregation + ">");
			process = rspEngine.process(e);
			rspEngine.progress(divider);

		} else if (tsEventNumber == 0) {
			currentResult.setInputTimestamp(System.currentTimeMillis());
			currentResult.setMemoryB(Memory.getMemoryUsage());
			currentResult.setEventNumber(eventNumber / aggregation);
			currentResult.setId("<http://example.org/" + experiment + "/" + eventNumber / aggregation + ">");
			tsEventNumber++;
			process = rspEngine.process(e);
		} else if (tsEventNumber == tsEventMax) {
			tsEventNumber = 0;
			process = rspEngine.process(e);
			rspEngine.progress(divider);
		} else {
			tsEventNumber++;
			process = rspEngine.process(e);
		}

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
