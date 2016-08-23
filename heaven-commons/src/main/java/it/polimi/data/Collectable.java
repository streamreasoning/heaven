package it.polimi.data;


import it.polimi.data.EngineEvent;

public interface Collectable extends EngineEvent {

    boolean save(String where);

}
