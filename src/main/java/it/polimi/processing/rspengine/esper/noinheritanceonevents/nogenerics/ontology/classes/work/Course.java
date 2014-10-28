package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.work;

public class Course extends Work {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5345174697483095245L;

	public Course(String object) {
		super(object);
	}

	public Course() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#Course");
	}
}
