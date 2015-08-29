package it.polimi.heaven.core.tsimpl.strategic.timecontrol;

import it.polimi.heaven.core.ts.events.HeavenResult;
import it.polimi.heaven.core.ts.events.Stimulus;
import it.polimi.heaven.core.ts.rspengine.RSPEngine;

public interface TimeStrategy {

	public boolean apply(Stimulus e);

	public void setRSPEngine(RSPEngine rspEngine);

	public HeavenResult getResult();

}
