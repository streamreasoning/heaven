package it.polimi.processing.ets.timecontrol;

import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TSResult;
import it.polimi.processing.rspengine.abstracts.RSPEngine;

public interface TimeStrategy {

	public boolean apply(RSPTripleSet e);

	public void setRSPEngine(RSPEngine rspEngine);

	public TSResult getResult();

}
