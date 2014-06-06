package rdf.museo.rdf.properties;

import rdf.museo.rdf.RDFClass;
import rdf.museo.rdf.RDFProperty;
import rdf.museo.rdf.RDFResource;

public class Domain extends RDFProperty<RDFProperty<?, ?>, RDFClass<?>> {

	public Domain(RDFProperty<RDFResource, RDFResource> domain,
			RDFClass<RDFResource> range, String property) {
		super(domain, range, property);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
