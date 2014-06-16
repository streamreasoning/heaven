package rdf.museo.ihneritance.generics.constrains.ontology.properties;

import rdf.museo.ihneritance.generics.ontology.Sculpt;
import rdf.museo.ihneritance.generics.ontology.Sculptor;
import rdf.museo.ihneritance.generics.rdfs.RDFClass;

public class Sculpts extends Creates {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculpts(RDFClass<Sculptor> sculptor, RDFClass<Sculpt> sculpt) {
		super(sculptor, sculpt, "sculpts");
	}
}
