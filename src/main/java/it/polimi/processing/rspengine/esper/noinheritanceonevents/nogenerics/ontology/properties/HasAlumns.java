package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization.University;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class HasAlumns extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HasAlumns() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#hasAlumnus", "http://swat.cse.lehigh.edu/onto/univ-bench.owl#University",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person", University.class, Person.class);
	}

	public HasAlumns(String property, String domainValue, String rangeValue, Class<? extends RDFResource> domain, Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
