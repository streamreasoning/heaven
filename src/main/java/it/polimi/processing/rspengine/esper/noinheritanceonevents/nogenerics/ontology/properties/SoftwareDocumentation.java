package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication.Publication;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication.Software;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class SoftwareDocumentation extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SoftwareDocumentation() {
		super(
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#softwareDocumentation",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Software",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Publication",
				Software.class, Publication.class);
	}

	public SoftwareDocumentation(String property, String domainValue,
			String rangeValue, Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
