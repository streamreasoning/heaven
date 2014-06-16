package rdf.museo.ihneritance.nogenerics.ontology;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;

public class Painter extends Artist {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Painter(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Painter " + getName();
	}

	@Override
	public RDFClass getSuper() {
		return new RDFClass(this.getClass().getSuperclass());
	}

}
