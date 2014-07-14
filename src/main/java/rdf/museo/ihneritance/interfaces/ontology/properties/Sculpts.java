package rdf.museo.ihneritance.interfaces.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.Sculpt;
import rdf.museo.ihneritance.nogenerics.ontology.Sculptor;

public class Sculpts extends Creates {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculpts(String property) {
		super(Sculptor.class, Sculpt.class, property);
	}

	public Sculpts() {
		this("sculpts");
	}
}
