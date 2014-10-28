package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.work;

public class GraduateCourse extends Course {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5194825937643484014L;

	public GraduateCourse(String object) {
		super(object);
	}

	public GraduateCourse() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#GraduateCourse");
	}
}
