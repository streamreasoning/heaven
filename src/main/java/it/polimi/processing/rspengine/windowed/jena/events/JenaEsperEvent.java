package it.polimi.processing.rspengine.windowed.jena.events;

import it.polimi.processing.events.TripleContainer;

import java.util.Set;

import com.hp.hpl.jena.graph.Graph;

public interface JenaEsperEvent {

	public Graph update(Graph abox);

	public Set<TripleContainer> serialize();

}
