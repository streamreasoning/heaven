package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization;

public class Department extends Organization {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2832786231226144043L;

	public Department(String object) {
		super(object);
	}

	public Department() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Department");
	}

}
