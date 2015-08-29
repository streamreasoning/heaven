package it.polimi.export.processing.rspengine.windowed.esper.plain.events;

import it.polimi.processing.events.TripleContainer;

import java.util.Set;

public interface TripleEvent {
	public Set<TripleContainer> getTriples();
}
