package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication;


public class Book extends Publication {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1143262403236858745L;

	public Book(String object) {
		super(object);
	}

	public Book() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Book");
	}
}
