package it.polimi.heaven.core.ts.collector;

import it.polimi.heaven.core.ts.events.engine.EngineEvent;


public interface Collectable extends EngineEvent {

	public boolean save(String where);

}
