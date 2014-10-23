package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Employee;

public class Faculty extends Employee {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Faculty(String object) {
		super(object);
		// TODO Auto-generated constructor stub
	}

	public Faculty() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Faculty");
	}

}
