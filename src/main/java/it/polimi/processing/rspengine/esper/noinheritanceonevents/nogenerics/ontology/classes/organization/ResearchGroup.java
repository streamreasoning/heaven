package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.organization;

public class ResearchGroup extends Organization {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9038218976462827028L;

	public ResearchGroup(String object) {
		super(object);
	}

	public ResearchGroup() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#ResearchGroup");
	}
}
