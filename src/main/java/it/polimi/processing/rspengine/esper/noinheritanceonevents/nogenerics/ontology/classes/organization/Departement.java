package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization;

public class Departement extends Organization {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2832786231226144043L;

	public Departement(String object) {
		super(object);
	}

	public Departement() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Department");
	}

}
