package rdf.museo.ihneritance.interfaces.esper.events;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFEvent;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class RDFSOut extends RDFEvent {

	public RDFSOut(RDFResource s, RDFProperty p, RDFResource o, long ts) {
		super(s, p, o, ts, "RDFSOut");
	}
}
