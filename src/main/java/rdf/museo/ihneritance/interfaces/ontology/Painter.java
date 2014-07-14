package rdf.museo.ihneritance.interfaces.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.interfaces.rdfs.RDFResource;

public class Painter extends Artist implements Serializable, RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Painter(String name) {
		super(Painter.class, name);
	}

	public Painter(Class<? extends Painter> clazz, String name) {
		super(clazz, name);
	}

	@Override
	public String toString() {
		return "Painter " + getName();
	}

}
