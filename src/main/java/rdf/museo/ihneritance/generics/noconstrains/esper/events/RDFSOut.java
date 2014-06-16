package rdf.museo.ihneritance.generics.noconstrains.esper.events;

import rdf.museo.ihneritance.generics.rdfs.RDFEvent;
import rdf.museo.ihneritance.generics.rdfs.RDFProperty;
import rdf.museo.ihneritance.generics.rdfs.RDFResource;

public class RDFSOut extends
		RDFEvent<RDFResource, RDFProperty<?, ?>, RDFResource> {

	public RDFSOut(RDFResource s, RDFProperty<?, ?> p, RDFResource o, long ts) {
		super(s, p, o, ts, "RDFSOut");
		// TODO Auto-generated constructor stub
	}

}
