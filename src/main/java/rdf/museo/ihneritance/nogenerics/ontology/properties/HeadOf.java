package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.organization.University;
import rdf.museo.ihneritance.nogenerics.ontology.classes.person.Person;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class HeadOf extends WorkFor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HeadOf() {
		super(University.class, Person.class, "is head of");
	}

	public HeadOf(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
