package rdf.museo.ihneritance.nogenerics.ontology;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;

public class Sculpt extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculpt(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Sculpt " + getName();
	}

	@Override
	public RDFClass getSuper() {
		return new RDFClass(this.getClass().getSuperclass());
	}

}
