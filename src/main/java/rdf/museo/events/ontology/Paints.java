package rdf.museo.events.ontology;

import rdf.museo.events.rdfs.RDFClass;

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
