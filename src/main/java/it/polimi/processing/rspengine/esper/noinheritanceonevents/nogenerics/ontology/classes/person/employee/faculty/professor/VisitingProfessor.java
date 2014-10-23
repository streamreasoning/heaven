package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.professor;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty.Professor;

public class VisitingProfessor extends Professor {

	private static final long serialVersionUID = 2845788598546041966L;

	public VisitingProfessor(String object) {
		super(object);
		// TODO Auto-generated constructor stub
	}

	public VisitingProfessor() {
		super(
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#VisitingProfessor");
	}

}
