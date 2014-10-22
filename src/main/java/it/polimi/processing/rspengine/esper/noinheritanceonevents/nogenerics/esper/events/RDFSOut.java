package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper.events;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFEvent;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class RDFSOut extends RDFEvent {

	public RDFSOut(RDFResource s, RDFProperty p, RDFResource o, long ts) {
		super(s, p, o, ts, "RDFSOut");
	}
}