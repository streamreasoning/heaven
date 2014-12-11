package it.polimi.processing.workbench.timecontrol;

import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.rspengine.windowed.RSPEngine;
import it.polimi.processing.workbench.core.RSPTestStand;
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
		RSPEngine rspEngine = ts.getRspEngine();
		rspEngine.progress(aggregation);
		boolean process = false;
		double memoryUsage = Memory.getMemoryUsage();
		long currentTimeMillis = System.currentTimeMillis();
		if (i == 0) {
			log.debug("Memory Before Sending [" + memoryUsage + "] On Event " + numberEvents);
			ts.setMemoryB(memoryUsage);
			ts.setTimestamp(currentTimeMillis);
			process = rspEngine.process(e);
			i++;
		} else if (i == aggregation - 1) {
			log.debug("Memory After Sending [" + memoryUsage + "] On Event " + numberEvents);
			ts.setMemoryA(memoryUsage);
			ts.setResultTimestamp(currentTimeMillis);
			process = rspEngine.process(e);
			process = ts.processDone();
			i = 0;
		} else {
			process = rspEngine.process(e);
			i++;
		}

		numberEvents++;

		// for rspesperengine move times forward according to the
		// size of the current window
		return process;
	}
}
