package rdf.museo.inheritanceOnEvents.activeEvents.esper.events;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Artist;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Piece;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Creates;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFEvent;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFProperty;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFResource;

public class RDFSInput extends
		WrapperEvent<RDFEvent<RDFResource, RDFProperty, RDFResource>> {

	protected long ts;

	public RDFSInput(RDFEvent<RDFResource, RDFProperty, RDFResource> e) {
		super(e);
		ts = e.getTimestamp();
	}

	public RDFResource getS() {
		return e.getS();
	}

	public RDFResource getO() {
		return e.getO();
	}

	public RDFProperty getP() {
		return e.getP();
	}

	public String getChannel() {
		return e.getChannel();
	}

	public RDFEvent<? extends Artist, ? extends Creates, ? extends Piece> getSuperEvent() {
		return e.getSuperEvent();
	}

	@Override
	public String toString() {
		return "RDFSInput contains: " + e + " time: " + ts;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

}
