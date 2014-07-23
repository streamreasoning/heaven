package rdf.museo.ihneritance.interfaces.rdfs;

import java.io.Serializable;

public class RDFClass implements Serializable, RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Class<?> clazz;
	protected String value;

	public RDFClass(Class<?> class1, String value) {
		this.clazz = class1;
		this.value = value;
	}

	@Override
	public String toString() {
		if (clazz != null)
			return clazz.getSimpleName();
		else
			return this.getClass().getCanonicalName();
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
