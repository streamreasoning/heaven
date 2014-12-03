package it.polimi.processing.rspengine.windowed.jena.events;

import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.rspengine.windowed.jena.JenaEsperEvent;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

@Data
public class StatementEvent implements JenaEsperEvent {

	private Statement statement;
	private long appTimestamp;
	private long timestamp;

	public Resource getS() {
		return statement.getSubject();
	}

	public Property getP() {
		return statement.getPredicate();
	}

	public RDFNode getO() {
		return statement.getObject();
	}

	@Override
	public Graph update(Graph abox) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TripleContainer> serialize() {
		HashSet<TripleContainer> hashSet = new HashSet<TripleContainer>();
		hashSet.add(new TripleContainer(getS().toString(), getP().toString(), getO().toString()));
		return hashSet;
	}
}
