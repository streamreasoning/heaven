package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication.Publication;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.work.Research;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class PublicationResearch extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PublicationResearch() {
		super(Publication.class, Research.class, "is being advised by");
	}

	public PublicationResearch(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
