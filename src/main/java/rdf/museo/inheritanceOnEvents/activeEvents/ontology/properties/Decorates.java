package rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Decorator;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Mosaic;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Sculpt;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Sculptor;

public class Decorates extends Sculpts {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Decorates() {
		super(Decorator.class, Mosaic.class, "decorates");
	}

	public Decorates(Class<? extends Decorator> range,
			Class<? extends Mosaic> domain) {
		super(range, domain, "decorates");
	}

	public Decorates(Class<Sculptor> sculptor, Class<Sculpt> sculpt, String prop) {
		super(sculptor, sculpt, prop);
	}
}
