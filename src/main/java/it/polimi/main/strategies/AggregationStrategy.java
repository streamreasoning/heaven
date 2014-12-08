package it.polimi.main.strategies;

import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.rspengine.windowed.RSPEngine;
import it.polimi.processing.workbench.core.RSPTestStand;
import it.polimi.processing.workbench.core.TimeStrategy;
import it.polimi.utils.Memory;
import lombok.extern.log4j.Log4j;

@Log4j
public class AggregationStrategy implements TimeStrategy {

	private int numberEvents = 0;
	private final int aggregation;
	int i;

	public AggregationStrategy(int aggreation) {
		this.aggregation = aggreation;
		this.i = 0;
	}

	@Override
	public boolean apply(RSPEvent e, RSPTestStand ts) {
		boolean process = false;
		RSPEngine rspEngine = ts.getRspEngine();

		if (i == 0) {
			double memoryUsage = Memory.getMemoryUsage();
			log.debug("Memory Before Sending [" + memoryUsage + "] On Event " + numberEvents);
			ts.setMemoryB(memoryUsage);
			ts.setTimestamp(System.currentTimeMillis());
			process = rspEngine.process(e);
			i++;
		} else if (i == aggregation - 1) {
			double memoryUsage = Memory.getMemoryUsage();
			log.debug("Memory After Sending [" + memoryUsage + "] On Event " + numberEvents);
			process = rspEngine.process(e);
			process = ts.processDone();
			i = aggregation;
		} else {
			process = rspEngine.process(e);
			i++;
		}

		numberEvents++;
		rspEngine.progress(aggregation);
		// for rspesperengine move times forward according to the
		// size of the current window
		return process;
	}
}
