package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class Organization extends RDFResource {

	private static final long serialVersionUID = 1L;

	public Organization(String object) {
		super(object);
	}

	public Organization() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Organization");
	}
}
