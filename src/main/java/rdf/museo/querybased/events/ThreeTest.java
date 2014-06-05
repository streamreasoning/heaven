package rdf.museo.querybased.events;

import rdf.RDFEvent;
import rdf.museo.rdf.RDFProperty;
import rdf.museo.rdf.RDFResource;

public class ThreeTest
		extends
		RDFEvent<RDFResource, RDFProperty<RDFResource, RDFResource>, RDFResource> {

	public ThreeTest(RDFResource s, RDFProperty<RDFResource, RDFResource> p,
			RDFResource o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "threetest [s=" + getS() + ", p=" + getP() + ", o=" + getC()
				+ "]";
	}

}
