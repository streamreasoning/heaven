package rdf.museo.ihneritance.interfaces.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.interfaces.rdfs.RDFResource;

public class Sculpt extends Piece implements Serializable, RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculpt(String name) {
		super(Sculpt.class, name);
	}

	public Sculpt(Class<? extends Sculpt> clazz, String name) {
		super(clazz, name);
	}

	@Override
	public String toString() {
		return "Sculpt " + getName();
	}

}
