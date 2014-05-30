package rdf.museo.querybased.events;

import rdf.RDFEvent;
import rdf.museo.rdf.RDFObject;
import rdf.museo.rdf.RDFProperty;

public class RDFSInput extends
		RDFEvent<RDFObject, RDFProperty<RDFObject>, RDFObject> {

	public RDFSInput(RDFObject s, RDFProperty<RDFObject> p, RDFObject o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
