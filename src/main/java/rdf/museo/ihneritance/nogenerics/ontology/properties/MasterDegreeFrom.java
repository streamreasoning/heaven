package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.organization.University;
import rdf.museo.ihneritance.nogenerics.ontology.classes.person.Person;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class MasterDegreeFrom extends DegreeFrom{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MasterDegreeFrom() {
		super(Person.class, University.class, "has a master degree from");
	}

	public MasterDegreeFrom(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
