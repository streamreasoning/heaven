package rdf.museo.ihneritance.interfaces.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.Artist;
import rdf.museo.ihneritance.nogenerics.ontology.Piece;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class Creates extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Creates() {
		super(Artist.class, Piece.class, "creates");
	}

	public Creates(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
