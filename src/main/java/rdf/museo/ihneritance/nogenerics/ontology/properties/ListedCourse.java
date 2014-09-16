package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.schedule.Schedule;
import rdf.museo.ihneritance.nogenerics.ontology.classes.work.Course;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class ListedCourse extends RDFProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListedCourse() {
		super(Schedule.class, Course.class, "lists as a course");
	}

	public ListedCourse(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
