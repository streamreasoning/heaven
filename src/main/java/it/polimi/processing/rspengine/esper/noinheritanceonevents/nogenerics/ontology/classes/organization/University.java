package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization;


public class University extends Organization {

	/**
	 * 
	 */
	private static final long serialVersionUID = 536825014279013109L;

	public University(String object) {
		super(object);
	}

	public University() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#University");
	}
}
