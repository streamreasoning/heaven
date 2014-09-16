package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.organization.Organization;
import rdf.museo.ihneritance.nogenerics.ontology.classes.publication.Publication;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class OrgPubblication extends RDFProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrgPubblication() {
		super(Organization.class, Publication.class, "publishes");
	}

	public OrgPubblication(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
