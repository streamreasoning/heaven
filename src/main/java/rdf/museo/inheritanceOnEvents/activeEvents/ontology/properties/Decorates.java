package rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Decorator;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Mosaic;

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

	public Decorates(Class<Decorator> decorator, Class<Mosaic> mosaic,
			String prop) {
		super(decorator, mosaic, prop);
	}
}
