package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.Faculty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.work.Course;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class TeacherOf extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TeacherOf() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf", "http://swat.cse.lehigh.edu/onto/univ-bench.owl#Faculty",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Course", Faculty.class, Course.class);
	}

	public TeacherOf(String property, String domainValue, String rangeValue, Class<? extends RDFResource> domain, Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
