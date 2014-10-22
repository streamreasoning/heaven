package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication.Publication;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class PublicationAuthor extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PublicationAuthor() {
		super(
				Publication.class,
				it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person.class,
				"was written by");
	}

	public PublicationAuthor(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
