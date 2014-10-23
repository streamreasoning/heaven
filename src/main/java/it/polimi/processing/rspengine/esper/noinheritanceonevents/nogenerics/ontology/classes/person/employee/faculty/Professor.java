package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.Faculty;

public class Professor extends Faculty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Professor(String object) {
		super(object);
	}

	public Professor() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Professor");
	}

}
