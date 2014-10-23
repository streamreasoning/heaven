package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.professor;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.Professor;

public class Dean extends Professor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Dean(String object) {
		super(object);
	}

	public Dean() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Dean");
	}
}
