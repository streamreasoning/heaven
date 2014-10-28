package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.professor;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.Professor;

public class AssociateProfessor extends Professor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AssociateProfessor(String object) {
		super(object);
	}

	public AssociateProfessor() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#AssociateProfessor");
	}

}
