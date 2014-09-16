package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.organization.Organization;
import rdf.museo.ihneritance.nogenerics.ontology.classes.person.Person;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class AffiliateOf extends RDFProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AffiliateOf() {
		super(Organization.class, Person.class, "is affiliated with");
	}

	public AffiliateOf(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
