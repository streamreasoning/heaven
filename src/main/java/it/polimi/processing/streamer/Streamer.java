package it.polimi.processing.streamer;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.workbench.core.Startable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

public interface Streamer<Event> extends Startable<ExecutionState> {

	public void stream(BufferedReader br, int experimentNumber) throws IOException;

	public Event createEvent(String key, Set<String[]> triple, int eventNumber, int experimentNumber);
}
