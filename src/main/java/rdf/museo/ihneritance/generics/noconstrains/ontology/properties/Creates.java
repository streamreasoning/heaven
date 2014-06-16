package rdf.museo.ihneritance.generics.noconstrains.ontology.properties;

import rdf.museo.ihneritance.generics.ontology.Artist;
import rdf.museo.ihneritance.generics.ontology.Piece;
import rdf.museo.ihneritance.generics.rdfs.RDFClass;
import rdf.museo.ihneritance.generics.rdfs.RDFProperty;
import rdf.museo.ihneritance.generics.rdfs.RDFResource;

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
