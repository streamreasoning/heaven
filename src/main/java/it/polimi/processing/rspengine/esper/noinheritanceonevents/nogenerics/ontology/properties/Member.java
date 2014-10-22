package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization.Organization;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class Member extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Member() {
		super(
				Organization.class,
				it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person.class,
				"has a memember");
	}

	public Member(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
