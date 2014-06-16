package rdf.museo.ihneritance.generics.constrains.esper.events;

import rdf.museo.ihneritance.generics.constrains.ontology.properties.CreatesObject;
import rdf.museo.ihneritance.generics.ontology.Artist;
import rdf.museo.ihneritance.generics.ontology.Piece;
import rdf.museo.ihneritance.generics.rdfs.RDFEvent;

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
