package rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties;

import java.io.Serializable;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Artist;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Piece;
import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFProperty;

public class Creates extends RDFProperty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Creates() {
		super(Artist.class, Piece.class, "creates");
	}

	public Creates(Class<? extends Artist> domain, Class<? extends Piece> range) {
		super(domain, range, "creates");
	}

	public Creates(Class<? extends Artist> domain,
			Class<? extends Piece> range, String property) {
		super(domain, range, property);
	}

}
