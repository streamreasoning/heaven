package it.polimi.heaven.core.teststand.collector;

import it.polimi.heaven.core.teststand.events.engine.EngineEvent;


public interface Collectable extends EngineEvent {

	public boolean save(String where);

}
