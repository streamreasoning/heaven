package rdf.museo.inheritanceOnEvents.activeEvents.ontology;


public class Paint extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Paint(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Paint " + getName();
	}

}
