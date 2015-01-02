package it.polimi.processing.streamer;

import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionState;

import java.io.BufferedReader;

public interface Streamer<T> extends Startable<ExecutionState> {

	public void process(BufferedReader br, int experimentNumber);

}
