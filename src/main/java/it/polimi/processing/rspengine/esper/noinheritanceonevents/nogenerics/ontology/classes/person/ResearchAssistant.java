package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.person;

public class ResearchAssistant extends Person {

	/**
	 * 
	 */
	private static final long serialVersionUID = -930703384618090443L;

	public ResearchAssistant(String object) {
		super(object);
	}

	public ResearchAssistant() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#ResearchAssistant");
	}
}
