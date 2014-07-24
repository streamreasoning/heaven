package rdf.museo.inheritanceOnEvents.activeEvents.esper.events;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Artist;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Piece;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Creates;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFEvent;

public class CreatesEvent<S extends Artist, P extends Creates, O extends Piece>
		extends RDFEvent<Artist, Creates, Piece> {

	public CreatesEvent(S s, O o, long ts) {
		super(s, new Creates(), o, ts, "CreatesEvent");
	}

	public CreatesEvent(S s, O o, long ts, String variant) {
		super(s, new Creates(s.getClass(), o.getClass()), o, ts, "CreatesEvent");
	}

	public CreatesEvent(S s, P p, O o, long ts, String prop) {
		super(s, p, o, ts, prop);
	}

	@Override
	public RDFEvent<? extends Artist, ? extends Creates, ? extends Piece> getSuperEvent() {
		return this;

	}

	@Override
	public String toString() {
		return "CreatesEvent [s=" + getS() + ", p=" + getP() + ", o=" + getO()
				+ "ts= " + getTimestamp() + " ]";
	}

}
