package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.publication.Publication;
import rdf.museo.ihneritance.nogenerics.ontology.classes.publication.Software;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class SoftwareDocumentation extends RDFProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SoftwareDocumentation() {
		super(Software.class, Publication.class, "is documented in");
	}

	public SoftwareDocumentation(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
