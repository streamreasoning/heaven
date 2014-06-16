package rdf.museo.ihneritance.generics.noconstrains.ontology.properties;

import rdf.museo.ihneritance.generics.ontology.Paint;
import rdf.museo.ihneritance.generics.ontology.Painter;
import rdf.museo.ihneritance.generics.rdfs.RDFClass;

public class Paints extends Creates {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Paints(String property) {
		super(new RDFClass(Painter.class), new RDFClass(Paint.class), property);
	}

	public Paints() {
		this("paints");
	}

}
