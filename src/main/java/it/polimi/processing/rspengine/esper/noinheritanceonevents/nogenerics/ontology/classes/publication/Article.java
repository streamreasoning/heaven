package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication;

public class Article extends Publication {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6831356746701154971L;

	public Article(String object) {
		super(object);
	}

	public Article() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Article");
	}
}
