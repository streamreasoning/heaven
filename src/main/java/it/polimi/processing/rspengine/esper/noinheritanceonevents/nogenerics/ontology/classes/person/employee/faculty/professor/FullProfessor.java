package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.professor;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.Professor;

public class FullProfessor extends Professor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FullProfessor(String object) {
		super(object);
	}

	public FullProfessor() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#FullProfessor");
	}
}
