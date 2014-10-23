package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.work;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class Work extends RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3586590410511947774L;

	public Work(String object) {
		super(object);
	}

	public Work() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Work");
	}
}
