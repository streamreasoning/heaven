package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication;

public class TechnicalReport extends Article {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3920594744088128004L;

	public TechnicalReport(String object) {
		super(object);
	}

	public TechnicalReport() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#TechnicalReport");
	}
}
