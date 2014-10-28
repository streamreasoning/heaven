package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.professor;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.Professor;

public class AssistantProfessor extends Professor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AssistantProfessor(String object) {
		super(object);
	}

	public AssistantProfessor() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#AssistantProfessor");
	}
}
