package rdf.museo.inheritanceOnEvents.activeEvents.ontology;

import java.io.Serializable;

public class Mosaic extends Sculpt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Mosaic(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Mosaic " + getName();
	}

}
