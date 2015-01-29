package it.polimi.processing.teststand.core.strategic.timecontrol;

import it.polimi.processing.events.CTEvent;
import it.polimi.processing.events.results.TSResult;
import it.polimi.processing.rspengine.abstracts.RSPEngine;

public interface TimeStrategy {

	public boolean apply(CTEvent e);

	public void setRSPEngine(RSPEngine rspEngine);

	public TSResult getResult();

}
