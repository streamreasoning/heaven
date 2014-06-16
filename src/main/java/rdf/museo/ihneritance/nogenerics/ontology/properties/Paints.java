package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.Paint;
import rdf.museo.ihneritance.nogenerics.ontology.Painter;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;

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
