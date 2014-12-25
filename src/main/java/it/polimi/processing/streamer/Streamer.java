package it.polimi.processing.streamer;

import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.TripleContainer;

import java.io.BufferedReader;
import java.util.Set;

public interface Streamer<T> extends Startable<ExecutionState> {

	public void startStreamimng(BufferedReader br, int experimentNumber);

	public T createEvent(Set<TripleContainer> triple, int eventNumber, int experimentNumber);
}
