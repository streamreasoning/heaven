package rdf.museo.ihneritance.nogenerics.ontology.properties;

import rdf.museo.ihneritance.nogenerics.ontology.classes.person.Student;
import rdf.museo.ihneritance.nogenerics.ontology.classes.work.Course;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class TakesCourse extends RDFProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TakesCourse() {
		super(Student.class, Course.class, "is taking");
	}

	public TakesCourse(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		super(domain, range, property);
	}
}
