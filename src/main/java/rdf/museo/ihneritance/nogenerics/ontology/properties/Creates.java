package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.Artist;
import rdf.museo.ihneritance.nogenerics.ontology.Piece;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;

public class Creates extends RDFProperty {

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
