package rdf.museo.events.ontology;

import rdf.museo.events.rdfs.RDFProperty;

public class Creates<D extends Artist, R extends Piece> extends
		RDFProperty<Artist, Piece> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Creates(Class<? extends Artist> domain,
			Class<? extends Piece> range, String property) {
		super(domain, range, property);
	}

	public Creates(String property) {
		this(Artist.class, Piece.class, property);
	}

	public Creates() {
		this("creates");
	}
}
