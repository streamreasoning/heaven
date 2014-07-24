package rdf.museo.inheritanceOnEvents.activeEvents.ontology;

import java.io.Serializable;

public class Sculpt extends Piece implements Serializable {

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
