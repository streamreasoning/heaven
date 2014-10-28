package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class HeadOf extends WorksFor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HeadOf() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#headOf", "", "", null, null);
	}

	public HeadOf(String property, String domainValue, String rangeValue, Class<? extends RDFResource> domain, Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
