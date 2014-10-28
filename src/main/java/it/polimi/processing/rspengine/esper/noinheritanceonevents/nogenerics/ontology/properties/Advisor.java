package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.Professor;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class Advisor extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Advisor() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#advisor", "http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Professor", Person.class, Professor.class);
	}

	public Advisor(String property, String domainValue, String rangeValue, Class<? extends RDFResource> domain, Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}

}
