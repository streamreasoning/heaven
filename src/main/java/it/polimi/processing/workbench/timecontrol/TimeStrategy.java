package it.polimi.processing.workbench.timecontrol;

import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TSResult;
import it.polimi.processing.rspengine.windowed.RSPEngine;

public interface TimeStrategy {

	public boolean apply(RSPEvent e);

	public void setRSPEngine(RSPEngine rspEngine);

	public TSResult getResult();

}
