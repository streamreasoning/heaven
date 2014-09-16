package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.person.TeachingAssistant;
import rdf.museo.ihneritance.nogenerics.ontology.classes.work.Course;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class TeachingAssistantOf extends RDFProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TeachingAssistantOf() {
		super(TeachingAssistant.class, Course.class, "is a teaching assistant for");
	}

	public TeachingAssistantOf(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
