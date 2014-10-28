package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization.University;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.Person;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class DoctoralDegreeFrom extends DegreeFrom {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DoctoralDegreeFrom() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#doctoralDegreeFrom", "http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#University", Person.class, University.class);
	}

	public DoctoralDegreeFrom(String property, String domainValue, String rangeValue, Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
