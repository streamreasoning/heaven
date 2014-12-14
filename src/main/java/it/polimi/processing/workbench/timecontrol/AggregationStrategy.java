package it.polimi.processing.workbench.timecontrol;

import it.polimi.processing.events.RSPEvent;
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
	public boolean apply(RSPEvent e) {

		boolean process = false;
		int eventNumber = rspEngine.getEventNumber();

		if (tsEventNumber == 0) {
			currentResult.setInputTimestamp(System.currentTimeMillis());
			currentResult.setMemoryB(Memory.getMemoryUsage());
			currentResult.setEventNumber(eventNumber / aggregation);
			currentResult.setId("<http://example.org/" + experiment + "/" + eventNumber / aggregation + ">");
			tsEventNumber++;
		} else if (tsEventNumber == tsEventMax) {
			tsEventNumber = 0;
			rspEngine.progress(EsperUtils.WINDOW_SIZE / EsperUtils.OUTPUT_RATE);
		} else {
			tsEventNumber++;
		}

		return rspEngine.process(e);
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
