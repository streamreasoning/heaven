package rdf.museo.ihneritance.interfaces.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.interfaces.rdfs.RDFResource;

public class Paint extends Piece implements Serializable, RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Paint(String name) {
		super(Paint.class, name);
	}

	public Paint(Class<? extends Paint> clazz, String name) {
		super(clazz, name);
	}

	@Override
	public String toString() {
		return "Paint " + getName();
	}

}
