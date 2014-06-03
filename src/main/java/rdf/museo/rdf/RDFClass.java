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
	public RDFClass getRDFClass() {
		return new RDFClass(clazz.getSuperclass());
	}

	@Override
	public String toString() {
		return "RDFClass " + clazz.getSimpleName();
	}

	@Override
	public int hashCode() {
		return clazz.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass() || obj == null)
			return false;
		RDFClass other = (RDFClass) obj;
		return clazz.equals(other.getClass());
	}
}
