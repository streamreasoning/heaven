package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.organization.University;
import rdf.museo.ihneritance.nogenerics.ontology.classes.person.Person;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class DoctoralDegreeFrom extends DegreeFrom{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DoctoralDegreeFrom() {
		super(Person.class, University.class, "has a doctoral degree from");
	}

	public DoctoralDegreeFrom(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
