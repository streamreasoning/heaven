package rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Sculpt;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Sculptor;

public class Sculpts extends Creates {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculpts() {
		super(Sculptor.class, Sculpt.class, "sculpts");
	}

	public Sculpts(Class<? extends Sculptor> sculptor,
			Class<? extends Sculpt> sculpt) {
		super(sculptor, sculpt, "sculpts");
	}

	public Sculpts(Class<? extends Sculptor> sculptor,
			Class<? extends Sculpt> sculpt, String prop) {
		super(sculptor, sculpt, prop);
	}
}
