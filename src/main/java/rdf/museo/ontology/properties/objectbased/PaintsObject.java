package rdf.museo.ontology.properties.objectbased;

import rdf.museo.ontology.Paint;
import rdf.museo.ontology.Painter;
import rdf.museo.rdf.RDFClass;

public class PaintsObject extends CreatesObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PaintsObject(RDFClass<Painter> painter, RDFClass<Paint> paint) {
		super(painter, paint, "paints");
	}

}
