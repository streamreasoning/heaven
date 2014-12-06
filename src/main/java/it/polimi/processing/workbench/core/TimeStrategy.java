package it.polimi.processing.workbench.core;

import it.polimi.processing.events.RSPEvent;

public interface TimeStrategy {

	public boolean apply(RSPEvent e, RSPTestStand ts);

}
