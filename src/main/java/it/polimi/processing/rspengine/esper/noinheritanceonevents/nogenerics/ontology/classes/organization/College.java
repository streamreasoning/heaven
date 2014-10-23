package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization;

public class College extends Organization {

	private static final long serialVersionUID = 7766224602350140719L;

	public College(String object) {
		super(object);
	}

	public College() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#College");
	}

}
