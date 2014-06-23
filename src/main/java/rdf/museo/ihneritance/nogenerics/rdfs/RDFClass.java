package rdf.museo.ihneritance.nogenerics.rdfs;

public final class RDFClass extends RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Class<?> clazz;

	public RDFClass(Class<?> class1) {
		super("");
		this.clazz = class1;
	}

	@Override
	public RDFClass getSuper() {
		return new RDFClass(clazz.getSuperclass());
	}

	@Override
	public String toString() {
		if (clazz != null)
			return clazz.getSimpleName();
		else
			return this.getClass().getCanonicalName();
	}

	@Override
	public int hashCode() {
		if (clazz != null)
			return clazz.hashCode();
		else
			return "null".hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass() || obj == null) {
			return false;
		}
		RDFClass other = (RDFClass) obj;
		if (other != null && clazz != null)
			return other.clazz.equals(clazz);
		else
			return false;
	}

	public Class<?> getType() {
		return clazz;
	}
}
