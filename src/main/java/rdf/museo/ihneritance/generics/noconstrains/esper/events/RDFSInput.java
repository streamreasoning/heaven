package rdf.museo.ihneritance.generics.noconstrains.esper.events;

import rdf.museo.ihneritance.generics.rdfs.RDFEvent;
import rdf.museo.ihneritance.generics.rdfs.RDFProperty;
import rdf.museo.ihneritance.generics.rdfs.RDFResource;

public class RDFSInput
		extends
		RDFEvent<RDFResource, RDFProperty<? extends RDFResource, ? extends RDFResource>, RDFResource> {

	public RDFSInput(RDFResource s,
			RDFProperty<? extends RDFResource, ? extends RDFResource> p,
			RDFResource o, long ts) {
		super(s, p, o, ts, "RDFSInput");
		// TODO Auto-generated constructor stub
	}

}
