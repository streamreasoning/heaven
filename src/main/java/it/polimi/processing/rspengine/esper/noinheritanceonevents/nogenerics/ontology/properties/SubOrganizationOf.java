package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization.Organization;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class SubOrganizationOf extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubOrganizationOf() {
		super(
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#subOrganizationOf",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Organization",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Organization",
				Organization.class, Organization.class);
	}

	public SubOrganizationOf(String property, String domainValue,
			String rangeValue, Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
