package rdf.museo.ontology.properties.classbased;

import rdf.museo.ontology.Artist;
import rdf.museo.ontology.Piece;
import rdf.museo.rdf.RDFClass;
import rdf.museo.rdf.RDFProperty;
import rdf.museo.rdf.RDFResource;

public class Creates extends RDFProperty<RDFResource, RDFResource> {

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
