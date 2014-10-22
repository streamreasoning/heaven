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
		super(Software.class, Publication.class, "is documented in");
	}

	public SoftwareDocumentation(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}