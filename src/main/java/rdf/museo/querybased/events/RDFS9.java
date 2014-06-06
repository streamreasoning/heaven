package rdf.museo.querybased.events;

import rdf.RDFEvent;
import rdf.museo.rdf.RDFProperty;
import rdf.museo.rdf.RDFResource;

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
