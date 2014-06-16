package rdf.museo.ihneritance.nogenerics.rdfs;

public final class RDFClass extends RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Class<?> clazz;

	public RDFClass(Class<?> class1) {
		super(class1.getCanonicalName());
		this.clazz = class1;
	}

	@Override
	public RDFClass getSuper() {
		if (clazz.equals(RDFResource.class))
			return new RDFClass(RDFClass.class);
		else
			return new RDFClass(this.clazz.getSuperclass());
	}

	@Override
	public String toString() {
		return clazz.getSimpleName();
	}

	@Override
	public int hashCode() {
		return clazz.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass() || obj == null) {
			return false;
		}
		RDFClass other = (RDFClass) obj;
		return clazz.equals(other.clazz);
	}

	public Class<?> getType() {
		return clazz;
	}
}
