package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.organization.Organization;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class SubOrganizationOf extends RDFProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubOrganizationOf() {
		super(Organization.class, Organization.class, "is part of");
	}

	public SubOrganizationOf(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
