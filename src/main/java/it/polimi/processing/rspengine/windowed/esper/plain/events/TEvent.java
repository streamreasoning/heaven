package it.polimi.processing.rspengine.windowed.esper.plain.events;

import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.rspengine.windowed.jena.JenaEsperEvent;
import it.polimi.utils.RDFSUtils;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;

@Data
@AllArgsConstructor
public class TEvent implements JenaEsperEvent {
	String s, p, o;
	long timestamp, app_timestamp;
	String channel;

	@Override
	public String toString() {
		return "TEvent [s=" + s + ", p=" + p + ", o=" + o + "ts=" + timestamp + "app_ts=" + app_timestamp + "]";
	}

	public String[] getSs() {
		return new String[] { s };
	}

	public String[] getPs() {
		return new String[] { p };
	}

	public String[] getOs() {
		return new String[] { o };
	}

	@Override
	public Graph update(Graph abox) {
		Resource subject = ResourceFactory.createResource(getS());
		Property predicate = (getP() != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(getP()) : RDF.type;
		RDFNode object = ResourceFactory.createResource(getO());
		abox.add(ResourceFactory.createStatement(subject, predicate, object).asTriple());
		return abox;
	}

	@Override
	public Set<TripleContainer> serialize() {
		HashSet<TripleContainer> hashSet = new HashSet<TripleContainer>();
		hashSet.add(new TripleContainer(getS().toString(), getP().toString(), getO().toString()));
		return hashSet;
	}
}