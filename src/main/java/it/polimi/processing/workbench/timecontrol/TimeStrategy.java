package it.polimi.processing.workbench.timecontrol;

import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.workbench.core.RSPTestStand;

public interface TimeStrategy {

	public boolean apply(RSPEvent e, RSPTestStand teststand);

}
