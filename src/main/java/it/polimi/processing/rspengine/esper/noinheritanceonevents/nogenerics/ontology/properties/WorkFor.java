package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization.University;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class WorkFor extends MemberOf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WorkFor() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#worksFor",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Organization",
				University.class, Person.class);
	}

	public WorkFor(String property, String domainValue, String rangeValue,
			Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
