package it.polimi.processing.collector;

import it.polimi.processing.workbench.core.EventProcessor;

import java.io.IOException;

public interface ResultCollector<Event> extends EventProcessor<Event> {

	public boolean process(Event r, String where) throws IOException;

}
