package rdf.museo.ihneritance.generics.rdfs;

public final class RDFClass<T extends RDFResource> extends RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Class<T> clazz;

	public RDFClass(Class<T> class1) {
		super(class1.getCanonicalName());
		this.clazz = class1;
	}

	@Override
	public RDFClass<T> getRDFClass() {
		return this;
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
		@SuppressWarnings("unchecked")
		RDFClass<T> other = (RDFClass<T>) obj;
		return clazz.equals(other.clazz);
	}

	public Class<?> getType() {
		return clazz;
	}
}
