package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.faculty;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.employee.Faculty;

public class PostDoctorate extends Faculty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PostDoctorate(String object) {
		super(object);
	}

	public PostDoctorate() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#PostDoc");
	}
}
