package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.schedule;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class Schedule extends RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6161567867209125845L;

	public Schedule(String object) {
		super(object);
	}

	public Schedule() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Schedule");
	}
}
