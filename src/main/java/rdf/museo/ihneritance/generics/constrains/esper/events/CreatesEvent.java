package rdf.museo.ihneritance.generics.constrains.esper.events;

import rdf.museo.ihneritance.generics.constrains.ontology.properties.Creates;
import rdf.museo.ihneritance.generics.ontology.Artist;
import rdf.museo.ihneritance.generics.ontology.Piece;
import rdf.museo.ihneritance.generics.rdfs.RDFEvent;

public class CreatesEvent<S extends Artist, P extends Creates, C extends Piece>
		extends RDFEvent<Artist, Creates, Piece> {

	public CreatesEvent(Artist s, Piece o, long ts) {
		super(s, new Creates(s.getSuper(), o.getSuper()), o, ts, "CreatesEvent");
	}

	public CreatesEvent(S s, P p, C o, long ts, String ch) {
		super(s, p, o, ts, ch);
	}

	@Override
	public String toString() {
		return "CreatesEvent [s=" + getS() + ", p=" + getP() + ", o=" + getO()
				+ "]";
	}

}
