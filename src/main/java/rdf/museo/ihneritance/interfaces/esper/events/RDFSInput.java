package rdf.museo.ihneritance.interfaces.esper.events;

import rdf.museo.ihneritance.interfaces.rdfs.RDFEvent;
import rdf.museo.ihneritance.interfaces.rdfs.RDFProperty;
import rdf.museo.ihneritance.interfaces.rdfs.RDFResource;

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
