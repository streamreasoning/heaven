package rdf.museo.inheritanceOnEvents.activeEvents.esper.events;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Artist;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Piece;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Creates;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFEvent;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFProperty;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFResource;

public class RDFS3 extends RDFEvent<RDFResource, RDFProperty, RDFResource> {

	public RDFS3(RDFResource s, RDFProperty p, RDFResource o, long ts) {
		super(s, p, o, ts, "RDFS3");
	}

	@Override
	public RDFEvent<? extends Artist, ? extends Creates, ? extends Piece> getSuperEvent() {
		// TODO Auto-generated method stub
		return null;
	}

}
