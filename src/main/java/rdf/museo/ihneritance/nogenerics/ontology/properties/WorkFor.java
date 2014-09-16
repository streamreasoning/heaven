package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.organization.University;
import rdf.museo.ihneritance.nogenerics.ontology.classes.person.Person;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class WorkFor extends MemberOf{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WorkFor() {
		super(University.class, Person.class, "works for");
	}

	public WorkFor(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
