package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person.student;

public class UndergraduateStudent extends Student {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UndergraduateStudent(String object) {
		super(object);
	}

	public UndergraduateStudent() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#UndergraduateStudent");
	}
}
