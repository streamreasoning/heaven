package rdf.museo.querybased.events;

import rdf.RDFEvent;
import rdf.museo.rdf.RDFObject;
import rdf.museo.rdf.RDFProperty;

public class RDFS3 extends
		RDFEvent<RDFObject, RDFProperty<RDFObject>, RDFObject> {

	public RDFS3(RDFObject s, RDFProperty<RDFObject> p, RDFObject o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
