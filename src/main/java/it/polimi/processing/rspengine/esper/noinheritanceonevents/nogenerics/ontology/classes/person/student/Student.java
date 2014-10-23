package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.student;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person;

public class Student extends Person {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Student(String object) {
		super(object);
	}

	public Student() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Student");
	}
}
