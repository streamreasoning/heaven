package rdf.museo.events.rdfs;

public class TypeOf extends RDFProperty<Object, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeOf(Class<?> domain, Class<?> range, String property) {
		super(domain, range, property);
	}

	public TypeOf() {
		super(Object.class, Object.class, "typeOf");
	}

}
