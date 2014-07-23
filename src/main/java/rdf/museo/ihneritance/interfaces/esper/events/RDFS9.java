package rdf.museo.ihneritance.interfaces.esper.events;

import rdf.museo.ihneritance.interfaces.rdfs.RDFEvent;
import rdf.museo.ihneritance.interfaces.rdfs.RDFProperty;
import rdf.museo.ihneritance.interfaces.rdfs.RDFResource;

public class RDFS9 extends RDFEvent {

	public RDFS9(RDFResource s, RDFProperty p, RDFResource o, long ts) {
		super(s, p, o, ts, "RDFS9");
	}

}
