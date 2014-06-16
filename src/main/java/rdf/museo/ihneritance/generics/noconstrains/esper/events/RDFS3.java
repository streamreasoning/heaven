package rdf.museo.ihneritance.generics.noconstrains.esper.events;

import rdf.museo.ihneritance.generics.rdfs.RDFEvent;
import rdf.museo.ihneritance.generics.rdfs.RDFProperty;
import rdf.museo.ihneritance.generics.rdfs.RDFResource;

public class RDFS3
		extends
		RDFEvent<RDFResource, RDFProperty<RDFResource, RDFResource>, RDFResource> {

	public RDFS3(RDFResource s, RDFProperty<RDFResource, RDFResource> p,
			RDFResource o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDFS3 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
