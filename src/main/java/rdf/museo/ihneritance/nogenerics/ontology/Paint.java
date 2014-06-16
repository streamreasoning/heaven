package rdf.museo.ihneritance.nogenerics.ontology;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;

public class Paint extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Paint(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Paint " + getName();
	}

	@Override
	public RDFClass getSuper() {
		return new RDFClass(this.getClass().getSuperclass());
	}

}
