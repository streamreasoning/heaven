package rdf.museo.ihneritance.generics.ontology;


public class Sculpt extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculpt(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Sculpt " + getName();
	}

}
