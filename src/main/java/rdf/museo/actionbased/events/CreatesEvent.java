package rdf.museo.actionbased.events;

import rdf.RDFEvent;
import rdf.museo.ontology.Artist;
import rdf.museo.ontology.Piece;
import rdf.museo.ontology.properties.Creates;

public class CreatesEvent<S extends Artist, P extends Creates, C extends Piece>
		extends RDFEvent<Artist, Creates, Piece> {

	public CreatesEvent(Artist s, Creates p, Piece o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
