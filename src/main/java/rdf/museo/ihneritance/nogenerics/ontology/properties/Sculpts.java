package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.Sculpt;
import rdf.museo.ihneritance.nogenerics.ontology.Sculptor;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;

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
