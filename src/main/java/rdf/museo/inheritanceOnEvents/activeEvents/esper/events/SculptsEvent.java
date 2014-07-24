package rdf.museo.inheritanceOnEvents.activeEvents.esper.events;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Artist;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Piece;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Sculpt;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Sculptor;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Creates;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Sculpts;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFEvent;

public class SculptsEvent<S extends Sculptor, P extends Sculpts, O extends Sculpt>
		extends CreatesEvent<Sculptor, Sculpts, Sculpt> {

	public SculptsEvent(S s, O o, long ts) {
		super(s, new Sculpts(), o, ts, "SculptsEvent");
	}

	public SculptsEvent(S s, O o, long ts, String variant) {
		super(s, new Sculpts(s.getClass(), o.getClass()), o, ts, "SculptsEvent");
	}

	public SculptsEvent(S s, P p, O o, long ts, String prop) {
		super(s, p, o, ts, prop);
	}

	@Override
	public RDFEvent<? extends Artist, ? extends Creates, ? extends Piece> getSuperEvent() {
		return new CreatesEvent<Artist, Creates, Piece>(new Artist(getS()
				.getName()), new Piece(getO().getName()), getTimestamp());
	}

	@Override
	public String toString() {
		return "SculptsEvent [s=" + getS() + ", p=" + getP() + ", o=" + getO()
				+ "ts= " + getTimestamp() + "]";
	}

}
