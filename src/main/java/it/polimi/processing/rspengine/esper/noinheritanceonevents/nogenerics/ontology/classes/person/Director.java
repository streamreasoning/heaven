package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person;


public class Director extends Person {

	private static final long serialVersionUID = 1L;

	public Director(String object) {
		super(object);
	}

	public Director() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Director");
	}

}
