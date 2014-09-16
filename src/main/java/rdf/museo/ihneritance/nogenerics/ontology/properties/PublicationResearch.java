package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.publication.Publication;
import rdf.museo.ihneritance.nogenerics.ontology.classes.work.Research;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class PublicationResearch extends RDFProperty{

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
