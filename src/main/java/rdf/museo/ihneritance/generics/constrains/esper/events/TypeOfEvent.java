package rdf.museo.ihneritance.generics.constrains.esper.events;

import rdf.museo.ihneritance.generics.ontology.properties.TypeOf;
import rdf.museo.ihneritance.generics.rdfs.RDFClass;
import rdf.museo.ihneritance.generics.rdfs.RDFEvent;
import rdf.museo.ihneritance.generics.rdfs.RDFResource;

public class TypeOfEvent extends
		RDFEvent<RDFResource, TypeOf, RDFClass<? extends RDFResource>> {

	public TypeOfEvent(RDFResource s, RDFClass<? extends RDFResource> o) {
		super(s, new TypeOf(s.getRDFClass(), o.getRDFClass()), o);
	}

	@Override
	public String toString() {
		return "TypeOfEvent [s=" + getS() + ", p=" + getP() + ", o=" + getC()
				+ "]";
	}

}
