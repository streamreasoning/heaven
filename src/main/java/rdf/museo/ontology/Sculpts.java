package rdf.museo.ontology;

import rdf.museo.rdf.RDFClass;

public class Sculpts extends Creates {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculpts(String property) {
		super(new RDFClass(Sculptor.class), new RDFClass(Sculpt.class),
				property);
	}

	public Sculpts() {
		this("scuplts");
	}
}
