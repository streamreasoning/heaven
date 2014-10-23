package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class TakesCourse extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TakesCourse() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#takesCourse", "",
				"", null, null);
	}

	public TakesCourse(String property, String domainValue, String rangeValue,
			Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
