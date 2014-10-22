package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper.events;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFEvent;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

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
