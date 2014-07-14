package rdf.museo.ihneritance.interfaces.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.interfaces.rdfs.RDFResource;

public class Sculptor extends Artist implements Serializable, RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculptor(String name) {
		super(Sculptor.class, name);
	}

	public Sculptor(Class<? extends Sculptor> clazz, String name) {
		super(clazz, name);
	}

	@Override
	public String toString() {

		return "Sculptor " + getName();
	}

}
