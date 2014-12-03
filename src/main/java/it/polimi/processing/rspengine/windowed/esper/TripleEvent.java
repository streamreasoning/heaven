package it.polimi.processing.rspengine.windowed.esper;

import it.polimi.processing.events.TripleContainer;

import java.util.Set;

public interface TripleEvent {
	public Set<TripleContainer> getTriples();
}
