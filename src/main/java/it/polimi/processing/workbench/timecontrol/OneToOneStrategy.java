package it.polimi.processing.workbench.timecontrol;

import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.rspengine.windowed.RSPEngine;
import it.polimi.processing.workbench.core.RSPTestStand;
import it.polimi.utils.Memory;

public class OneToOneStrategy implements TimeStrategy {

	@Override
	public boolean apply(RSPEvent e, RSPTestStand ts) {
		boolean process = false;
		RSPEngine rspEngine = ts.getRspEngine();
		ts.setMemoryB(Memory.getMemoryUsage());
		ts.setTimestamp(System.currentTimeMillis());
		process = rspEngine.process(e);

		ts.setMemoryA(Memory.getMemoryUsage());

		process = ts.processDone();

		rspEngine.progress(1);
		return process;
	}

}
