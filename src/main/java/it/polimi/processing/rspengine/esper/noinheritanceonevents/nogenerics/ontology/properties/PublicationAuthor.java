package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication.Publication;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class PublicationAuthor extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PublicationAuthor() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://swat.cse.lehigh.edu/onto/univ-bench.owl#Publication",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person", Publication.class, Person.class);
	}

	public PublicationAuthor(String property, String domainValue, String rangeValue, Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
