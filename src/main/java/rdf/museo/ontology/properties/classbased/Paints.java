package rdf.museo.ontology.properties.classbased;

import rdf.museo.ontology.Paint;
import rdf.museo.ontology.Painter;
import rdf.museo.rdf.RDFClass;

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
