package rdf.museo.ontology;

import rdf.museo.rdf.RDFClass;
import rdf.museo.rdf.RDFObject;
import rdf.museo.rdf.RDFProperty;

public class Creates extends RDFProperty<RDFObject> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Creates(RDFClass domain, RDFClass range, String property) {
		super(domain, range, property);
	}

	public Creates(String property) {
		this(new RDFClass(Artist.class), new RDFClass(Piece.class), property);
	}

	public Creates() {
		this("creates");
	}
}
