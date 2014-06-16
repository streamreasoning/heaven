package rdf.museo.ihneritance.generics.constrains.ontology.properties;

import rdf.museo.ihneritance.generics.ontology.Paint;
import rdf.museo.ihneritance.generics.ontology.Painter;
import rdf.museo.ihneritance.generics.rdfs.RDFClass;

public class PaintsObject extends CreatesObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PaintsObject(RDFClass<Painter> painter, RDFClass<Paint> paint) {
		super(painter, paint, "paints");
	}

}
