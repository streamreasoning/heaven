package rdf.museo.querybased.events;

import rdf.museo.propertybased.events.Sendable;
import rdf.museo.rdf.RDFEvent;
import rdf.museo.rdf.RDFProperty;
import rdf.museo.rdf.RDFResource;

public class RDFSOut extends
		RDFEvent<RDFResource, RDFProperty<?, ?>, RDFResource> implements
		Sendable<RDFResource, RDFProperty<?, ?>, RDFResource> {

	public RDFSOut(RDFResource s, RDFProperty<RDFResource, RDFResource> p,
			RDFResource o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "Out [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
