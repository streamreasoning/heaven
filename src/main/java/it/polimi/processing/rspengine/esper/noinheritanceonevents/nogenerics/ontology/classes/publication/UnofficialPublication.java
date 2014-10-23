package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication;


public class UnofficialPublication extends Publication {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5111307026796839869L;

	public UnofficialPublication(String object) {
		super(object);
	}

	public UnofficialPublication() {
		super(
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#UnofficialPublication");
	}
}
