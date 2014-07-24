package rdf.museo.inheritanceOnEvents.activeEvents.ontology;

import java.io.Serializable;

public class Sculptor extends Artist implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculptor(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Sculptor " + getName();
	}

}
