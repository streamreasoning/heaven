package rdf.museo.ontology.properties.objectbased;

import rdf.museo.ontology.Sculpt;
import rdf.museo.ontology.Sculptor;
import rdf.museo.rdf.RDFClass;

public class SculptsObject extends CreatesObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SculptsObject(RDFClass<Sculptor> sculptor, RDFClass<Sculpt> sculpt) {
		super(sculptor, sculpt, "sculpts");
	}
}
