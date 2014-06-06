package rdf.museo.rdf;

import java.io.Serializable;

public abstract class RDFResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String value;

	public RDFResource(String object) {
		this.setValue(object);
	}

	public RDFClass getRDFClass() {
		return new RDFClass(this.getClass());
	};

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (!(obj instanceof RDFResource))
			return false;
		else {
			RDFResource other = (RDFResource) obj;
			return getValue().equals(other.getValue());
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
