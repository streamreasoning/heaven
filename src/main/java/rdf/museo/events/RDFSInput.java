package rdf.museo.events.querybased;

import rdf.museo.events.RDFEvent;
import rdf.museo.events.rdfs.RDFObject;
import rdf.museo.events.rdfs.RDFProperty;

public class RDFSInput extends
		RDFEvent<RDFObject, RDFProperty<RDFObject>, RDFObject> {

	public RDFSInput(RDFObject s, RDFProperty<RDFObject> p, RDFObject o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + s + ", p=" + p + ", o=" + c + "]";
	}

}
