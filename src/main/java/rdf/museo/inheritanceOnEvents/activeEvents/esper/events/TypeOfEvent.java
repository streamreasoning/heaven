package rdf.museo.inheritanceOnEvents.activeEvents.esper.events;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Artist;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Piece;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Creates;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.TypeOf;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFClass;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFEvent;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFResource;

public class TypeOfEvent extends
		RDFEvent<RDFResource, TypeOf, RDFClass<? extends RDFResource>> {

	public TypeOfEvent(RDFResource s, RDFClass<? extends RDFResource> o, long ts) {
		super(s,
				new TypeOf(s.getClass(),
						(Class<? extends RDFClass<? extends RDFResource>>) o
								.getClass()), o, ts, "TypeOfEvent");
	}

	@Override
	public String toString() {
		return "TypeOfEvent [c=" + getS() + ", p=" + getP() + ", o=" + getO()
				+ ", " + getChannel() + ", ts=" + getTimestamp() + "]";
	}

	@Override
	public RDFEvent<? extends Artist, ? extends Creates, ? extends Piece> getSuperEvent() {
		// TODO Auto-generated method stub
		return null;
	}

}
