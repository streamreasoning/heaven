package rdf.museo.inheritanceOnEvents.activeEvents.esper.events;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Artist;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Decorator;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Mosaic;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Piece;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Sculpt;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Sculptor;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Creates;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Decorates;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Sculpts;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFEvent;

public class DecoratesEvent<S extends Decorator, P extends Decorates, O extends Mosaic>
		extends SculptsEvent<Sculptor, Sculpts, Sculpt> {

	public DecoratesEvent(S s, O o, long ts) {
		super(s, new Decorates(), o, ts, "DecoratesEvent");
	}

	public DecoratesEvent(S s, O o, long ts, String variant) {
		super(s, new Decorates(s.getClass(), o.getClass()), o, ts,
				"DecoratesEvent");
	}

	public DecoratesEvent(S s, P p, O o, long ts, String prop) {
		super(s, p, o, ts, prop);
	}

	@Override
	public RDFEvent<? extends Artist, ? extends Creates, ? extends Piece> getSuperEvent() {
		return new SculptsEvent<Sculptor, Sculpts, Sculpt>(new Sculptor(getS()
				.getValue()), new Sculpt(getO().getValue()), getTimestamp());
	}

	@Override
	public String toString() {
		return "DecoratesEvent [s=" + getS() + ", p=" + getP() + ", o="
				+ getO() + "ts= " + getTimestamp() + "]";
	}

}
