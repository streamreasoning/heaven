package rdf.museo.querybased.events;

import rdf.RDFEvent;
import rdf.museo.rdf.RDFProperty;
import rdf.museo.rdf.RDFResource;

public class RDFSInput
		extends
		RDFEvent<RDFResource, RDFProperty<RDFResource, RDFResource>, RDFResource> {

	public RDFSInput(RDFResource s, RDFProperty<RDFResource, RDFResource> p,
			RDFResource o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDFInput [s=" + getS() + ", p=" + getP() + ", o=" + getC()
				+ "]";
	}

}
