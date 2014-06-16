package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class TypeOf extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeOf(RDFClass domain, RDFClass range, String property) {
		super(domain, range, property);
	}

	public TypeOf(String property) {
		this(new RDFClass(RDFResource.class), new RDFClass(RDFClass.class),
				property);
	}

	public TypeOf() {
		this("typeOf");
	}
}
