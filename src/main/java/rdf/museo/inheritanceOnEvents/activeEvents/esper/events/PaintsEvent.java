package rdf.museo.inheritanceOnEvents.activeEvents.esper.events;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Artist;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Paint;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Painter;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Piece;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Creates;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Paints;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFEvent;

public class PaintsEvent<S extends Painter, P extends Paints, O extends Paint>
		extends CreatesEvent<Painter, Paints, Paint> {

	public PaintsEvent(S s, O o, long ts) {
		super(s, new Paints(), o, ts, "PaintsEvent");
	}

	public PaintsEvent(S s, O o, long ts, String variant) {
		super(s, new Paints(s.getClass(), o.getClass()), o, ts, "PaintsEvent");
	}

	public PaintsEvent(S s, P p, O o, long ts, String prop) {
		super(s, p, o, ts, prop);
	}

	@Override
	public String toString() {
		return "PaintsEvent [s=" + getS() + ", p=" + getP() + ", o=" + getO()
				+ "ts= " + getTimestamp() + "]";
	}

	@Override
	public RDFEvent<? extends Artist, ? extends Creates, ? extends Piece> getSuperEvent() {
		return new CreatesEvent<Artist, Creates, Piece>(getS(), getO(),
				getTimestamp());
	}
}
