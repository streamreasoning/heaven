package it.polimi.processing.rspengine.windowed.jena.events;

import it.polimi.processing.rspengine.windowed.jena.JenaEsperEvent;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

@Data
public class TripleEvent implements JenaEsperEvent {

	private Resource s;
	private Property p;
	private RDFNode o;

	private long appTimestamp;
	private long timestamp;

	@Override
	public Graph update(Graph abox) {
		abox.add(new Triple(s.asNode(), p.asNode(), o.asNode()));
		return abox;

	}

	@Override
	public Set<String[]> serialize() {
		HashSet<String[]> hashSet = new HashSet<String[]>();
		hashSet.add(new String[] { getS().toString(), getP().toString(), getO().toString() });
		return hashSet;
	}

}
