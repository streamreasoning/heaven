package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person;

public class Chair extends Person {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Chair(String object) {
		super(object);
	}

	public Chair() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Chair");
	}
}
