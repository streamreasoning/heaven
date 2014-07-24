package rdf.museo.inheritanceOnEvents.activeEvents.ontology;

import java.io.Serializable;

public class Decorator extends Sculptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Decorator(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Decorator " + getName();
	}

}
