package rdf.museo.propertybased.events;

import rdf.RDFEvent;
import rdf.museo.ontology.Artist;
import rdf.museo.ontology.Piece;
import rdf.museo.ontology.properties.objectbased.CreatesObject;

public class CreatesEvent<S extends Artist, P extends CreatesObject, C extends Piece>
		extends RDFEvent<Artist, CreatesObject, Piece> {

	public CreatesEvent(Artist s, Piece o) {
		super(s, new CreatesObject(s.getRDFClass(), o.getRDFClass()), o);
	}

	public CreatesEvent(S s, P p, C o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "CreatesEvent [s=" + getS() + ", p=" + getP() + ", o=" + getC()
				+ "]";
	}

}
