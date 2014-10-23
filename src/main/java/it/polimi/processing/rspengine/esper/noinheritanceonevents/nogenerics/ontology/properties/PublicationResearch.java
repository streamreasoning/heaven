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
		super(
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationResearch",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Publication",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Research",
				Publication.class, Research.class);
	}

	public PublicationResearch(String property, String domainValue,
			String rangeValue, Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
