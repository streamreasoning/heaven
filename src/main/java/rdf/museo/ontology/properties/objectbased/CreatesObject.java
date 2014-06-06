package rdf.museo.ontology.properties.objectbased;

import rdf.museo.ontology.Artist;
import rdf.museo.ontology.Piece;
import rdf.museo.rdf.RDFClass;
import rdf.museo.rdf.RDFProperty;

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
