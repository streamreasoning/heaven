package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.Paint;
import rdf.museo.ihneritance.nogenerics.ontology.Painter;

public class Paints extends Creates {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Paints(String property) {
		super(Painter.class, Paint.class, property);
	}

	public Paints() {
		this("paints");
	}

}
