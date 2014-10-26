package it.polimi.processing.streamer;

import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionStates;

import java.io.BufferedReader;
import java.io.IOException;

public interface Streamer extends Startable<ExecutionStates> {

	public void stream(BufferedReader br, int experimentNumber,
			String engineName, int tripleGraph) throws IOException;
}
