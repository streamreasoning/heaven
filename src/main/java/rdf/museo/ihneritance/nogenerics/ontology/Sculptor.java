package rdf.museo.ihneritance.nogenerics.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;

public class Sculptor extends Artist implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculptor(String name) {
		super(name);
	}

	@Override
	public String toString() {

		return "Sculptor " + getName();
	}

	@Override
	public RDFClass getSuper() {
		return new RDFClass(this.getClass().getSuperclass());
	}

}
