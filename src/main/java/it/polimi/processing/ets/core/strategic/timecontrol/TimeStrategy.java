package it.polimi.processing.ets.core.strategic.timecontrol;

import it.polimi.processing.events.InputRDFStream;
import it.polimi.processing.events.results.TSResult;
import it.polimi.processing.rspengine.abstracts.RSPEngine;

public interface TimeStrategy {

	public boolean apply(InputRDFStream e);

	public void setRSPEngine(RSPEngine rspEngine);

	public TSResult getResult();

}
