package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.person.Person;
import rdf.museo.ihneritance.nogenerics.ontology.classes.publication.Publication;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class PublicationAuthor extends RDFProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PublicationAuthor() {
		super(Publication.class, Person.class, "was written by");
	}

	public PublicationAuthor(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
