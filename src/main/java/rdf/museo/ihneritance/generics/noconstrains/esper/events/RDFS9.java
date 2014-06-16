package rdf.museo.ihneritance.generics.noconstrains.esper.events;

import rdf.museo.ihneritance.generics.rdfs.RDFEvent;
import rdf.museo.ihneritance.generics.rdfs.RDFProperty;
import rdf.museo.ihneritance.generics.rdfs.RDFResource;

public class RDFS9
		extends
		RDFEvent<RDFResource, RDFProperty<RDFResource, RDFResource>, RDFResource> {

	public RDFS9(RDFResource s, RDFProperty<RDFResource, RDFResource> p,
			RDFResource o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDFS9 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
