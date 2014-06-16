package rdf.museo.ihneritance.generics.noconstrains.esper.events;

import rdf.museo.ihneritance.generics.constrains.esper.events.Sendable;
import rdf.museo.ihneritance.generics.rdfs.RDFEvent;
import rdf.museo.ihneritance.generics.rdfs.RDFProperty;
import rdf.museo.ihneritance.generics.rdfs.RDFResource;

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
