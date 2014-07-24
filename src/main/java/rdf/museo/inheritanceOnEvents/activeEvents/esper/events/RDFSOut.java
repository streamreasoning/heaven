package rdf.museo.inheritanceOnEvents.activeEvents.esper.events;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Artist;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Piece;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Creates;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFEvent;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFProperty;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFResource;

public class RDFSOut extends RDFEvent<RDFResource, RDFProperty, RDFResource> {

	public RDFSOut(RDFResource s, RDFProperty p, RDFResource o, long ts) {
		super(s, p, o, ts, "RDFSOut");
		// TODO Auto-generated constructor stub
	}

	@Override
	public RDFEvent<? extends Artist, ? extends Creates, ? extends Piece> getSuperEvent() {
		// TODO Auto-generated method stub
		return null;
	}

}
