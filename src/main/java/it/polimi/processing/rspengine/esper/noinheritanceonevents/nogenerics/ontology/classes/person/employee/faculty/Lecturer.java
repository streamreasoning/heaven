package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.Faculty;

public class Lecturer extends Faculty {

	private static final long serialVersionUID = 1L;

	public Lecturer(String object) {
		super(object);
	}

	public Lecturer() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Lecturer");
	}
}
