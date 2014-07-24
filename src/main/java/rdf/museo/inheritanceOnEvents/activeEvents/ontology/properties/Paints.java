package rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Paint;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Painter;

public class Paints extends Creates {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Paints() {
		super(Painter.class, Paint.class, "paints");
	}

	public Paints(Class<? extends Painter> painter, Class<? extends Paint> paint) {
		super(painter, paint, "paints");
	}

	public Paints(Class<? extends Painter> painter,
			Class<? extends Paint> paint, String prop) {
		super(painter, paint, prop);
	}

}
