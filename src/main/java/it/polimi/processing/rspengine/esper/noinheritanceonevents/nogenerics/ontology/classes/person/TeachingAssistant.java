package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person;

public class TeachingAssistant extends Person {

	/**
	 * 
	 */
	private static final long serialVersionUID = -893300675785772197L;

	public TeachingAssistant(String object) {
		super(object);
	}

	public TeachingAssistant() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#TeachingAssistant");
	}
}
