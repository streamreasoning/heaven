package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.organization.University;
import rdf.museo.ihneritance.nogenerics.ontology.classes.person.Person;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class DegreeFrom extends RDFProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DegreeFrom() {
		super(Person.class, University.class, "has a degree from");
	}

	public DegreeFrom(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
