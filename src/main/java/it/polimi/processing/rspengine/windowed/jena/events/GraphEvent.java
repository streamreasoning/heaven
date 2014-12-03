package it.polimi.processing.rspengine.windowed.jena.events;

import it.polimi.processing.rspengine.windowed.jena.JenaEsperEvent;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphUtil;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.compose.Union;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

@Data
public class GraphEvent implements JenaEsperEvent {

	private Graph graph;
	private long appTimestamp;
	private long timestamp;

	@Override
	public Graph update(Graph abox) {
		return new Union(abox, graph);
	}

	@Override
	public Set<String[]> serialize() {
		HashSet<String[]> hashSet = new HashSet<String[]>();
		ExtendedIterator<Triple> all = GraphUtil.findAll(graph);
		while (all.hasNext()) {
			Triple next = all.next();
			hashSet.add(new String[] { next.getSubject().toString(), next.getPredicate().toString(), next.getObject().toString() });
		}

		return hashSet;
	}
}
