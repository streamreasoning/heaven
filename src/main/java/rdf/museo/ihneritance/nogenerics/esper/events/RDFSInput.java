package rdf.museo.ihneritance.nogenerics.esper.events;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFEvent;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class RDFSInput extends RDFEvent {

	public RDFSInput(RDFResource s, RDFProperty p, RDFResource o, long ts) {
		super(s, p, o, ts, "RDFSInput");
	}

	@Override
	public String toString() {
		return "RDFInput [s=" + getS() + ", p=" + getP() + ", o=" + getO()
				+ " ts= " + getTimestamp() + "]";
	}
}
