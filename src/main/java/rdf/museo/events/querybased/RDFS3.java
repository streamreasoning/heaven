package rdf.museo.events.querybased;

import rdf.museo.events.RDFEvent;
import rdf.museo.events.rdfs.RDFObject;
import rdf.museo.events.rdfs.RDFProperty;

public class RDFS3 extends
		RDFEvent<RDFObject, RDFProperty<RDFObject>, RDFObject> {

	public RDFS3(RDFObject s, RDFProperty<RDFObject> p, RDFObject o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + s + ", p=" + p + ", o=" + c + "]";
	}

}
