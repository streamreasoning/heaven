package rdf.museo.rdf;

public class RDFClass extends RDFObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Class<?> clazz;

	public RDFClass(Class<?> clazz) {
		super(clazz.getCanonicalName());
		this.clazz = clazz;
	}

	@Override
	public Class<?> getType() {
		return clazz;
	}

}
