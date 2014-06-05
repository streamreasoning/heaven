package rdf.museo.querybased.events;

import rdf.RDFEvent;
import rdf.museo.rdf.RDFProperty;
import rdf.museo.rdf.RDFResource;

public class RDFS3
		extends
		RDFEvent<RDFResource, RDFProperty<RDFResource, RDFResource>, RDFResource> {

	public RDFS3(RDFResource s, RDFProperty<RDFResource, RDFResource> p,
			RDFResource o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
