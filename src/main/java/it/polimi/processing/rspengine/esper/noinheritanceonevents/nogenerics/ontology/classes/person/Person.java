package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class Person extends RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6513018010592668846L;

	public Person(String object) {
		super(object);
	}

	public Person() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person");
	}
}
