package rdf.museo.propertybased.events;

import rdf.museo.rdf.RDFClass;
import rdf.museo.rdf.RDFEvent;
import rdf.museo.rdf.RDFResource;
import rdf.museo.rdf.properties.TypeOf;

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
