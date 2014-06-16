package rdf.museo.ihneritance.nogenerics.esper.events;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFEvent;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class RDFS9 extends RDFEvent {

	public RDFS9(RDFResource s, RDFProperty p, RDFResource o, long ts) {
		super(s, p, o, ts, "RDFS9");
	}

}
