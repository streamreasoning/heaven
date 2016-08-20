package it.polimi.heaven.core.teststand.collector;


import it.polimi.heaven.core.teststand.data.EngineEvent;

public interface Collectable extends EngineEvent {

    boolean save(String where);

}
