package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.work.Course;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class ListedCourse extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6861836888833237331L;

	public ListedCourse() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#listedCourse", "http://swat.cse.lehigh.edu/onto/univ-bench.owl#Schedule",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Course", Person.class, Course.class);
	}

	public ListedCourse(String property, String domainValue, String rangeValue, Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
