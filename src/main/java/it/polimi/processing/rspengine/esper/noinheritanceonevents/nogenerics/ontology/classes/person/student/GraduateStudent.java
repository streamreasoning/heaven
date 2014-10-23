package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.student;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person;


public class GraduateStudent extends Person {

	private static final long serialVersionUID = 1856015769399273520L;

	public GraduateStudent(String object) {
		super(object);
	}

	public GraduateStudent() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#GraduateStudent");
	}

}
