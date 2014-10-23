package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication;


public class ConferencePaper extends Article {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7966573194160592415L;

	public ConferencePaper(String object) {
		super(object);
	}

	public ConferencePaper() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#ConferencePaper");
	}
}
