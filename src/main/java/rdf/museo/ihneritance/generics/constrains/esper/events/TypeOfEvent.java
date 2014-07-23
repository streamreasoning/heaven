package rdf.museo.ihneritance.generics.constrains.esper.events;

import rdf.museo.ihneritance.generics.ontology.TypeOf;
import rdf.museo.ihneritance.generics.rdfs.RDFClass;
import rdf.museo.ihneritance.generics.rdfs.RDFEvent;
import rdf.museo.ihneritance.generics.rdfs.RDFResource;

public class TypeOfEvent extends
		RDFEvent<RDFResource, TypeOf, RDFClass<? extends RDFResource>> {

	public TypeOfEvent(RDFResource s, RDFClass<? extends RDFResource> o, long ts) {
		super(s, new TypeOf(s, o), o, ts, "TypeOfEvent");
	}

	@Override
	public String toString() {
		return "TypeOfEvent [c=" + getS() + ", p=" + getP() + ", o=" + getO()
				+ ", " + getChannel() + ", ts=" + getTimestamp() + "]";
	}

}
