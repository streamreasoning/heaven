package it.polimi.processing.rspengine.windowed.jena;

import java.util.Set;

import com.hp.hpl.jena.graph.Graph;

public interface JenaEsperEvent {

	public Graph update(Graph abox);

	public Set<String[]> serialize();

}
