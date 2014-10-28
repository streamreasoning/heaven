package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties;

import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.TeachingAssistant;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.work.Course;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;

public class TeachingAssistantOf extends RDFProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TeachingAssistantOf() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teachingAssistantOf",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#TeachingAssistant", "http://swat.cse.lehigh.edu/onto/univ-bench.owl#Course",
				TeachingAssistant.class, Course.class);
	}

	public TeachingAssistantOf(String property, String domainValue, String rangeValue, Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range) {
		super(property, domainValue, rangeValue, domain, range);
	}
}
