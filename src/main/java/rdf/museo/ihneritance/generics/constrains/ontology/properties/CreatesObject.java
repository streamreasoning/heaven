package rdf.museo.ihneritance.generics.constrains.ontology.properties;

import rdf.museo.ihneritance.generics.ontology.Artist;
import rdf.museo.ihneritance.generics.ontology.Piece;
import rdf.museo.ihneritance.generics.rdfs.RDFClass;
import rdf.museo.ihneritance.generics.rdfs.RDFProperty;

public class CreatesObject extends
		RDFProperty<RDFClass<? extends Artist>, RDFClass<? extends Piece>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CreatesObject(RDFClass<Artist> domain, RDFClass<Piece> range) {
		super(domain, range, "creates");
	}

	public CreatesObject(RDFClass<? extends Artist> domain,
			RDFClass<? extends Piece> range, String property) {
		super(domain, range, property);
	}

}
