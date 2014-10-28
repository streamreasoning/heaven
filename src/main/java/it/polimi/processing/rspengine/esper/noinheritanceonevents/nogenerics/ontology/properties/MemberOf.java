package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization.Organization;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class MemberOf extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MemberOf() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#memberOf", "http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person", Person.class, Organization.class);
	}

	public MemberOf(String property, String domainValue, String rangeValue, Class<? extends RDFResource> domain, Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
