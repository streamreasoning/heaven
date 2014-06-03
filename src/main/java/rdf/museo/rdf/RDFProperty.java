package rdf.museo.rdf;

import java.io.Serializable;

public abstract class RDFProperty<T> implements Serializable {

	/**
	 * 
	 */
	static final long serialVersionUID = 1L;
	protected RDFClass range;
	protected RDFClass domain;
	protected String property;

	public RDFProperty(RDFClass domain, RDFClass range, String property) {
		this.range = range;
		this.domain = domain;
		this.property = property;
	}

	public RDFClass getRange() {
		return range;
	}

	public RDFClass getDomain() {
		return domain;

	}

	@Override
	public String toString() {
		return property;
	}

	@Override
	public int hashCode() {
		return domain.hashCode() + range.hashCode() + property.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof RDFProperty) {
			@SuppressWarnings("unchecked")
			RDFProperty<T> other = (RDFProperty<T>) obj;
			return other.getDomain().equals(domain)
					&& other.getRange().equals(range)
					&& other.property.equals(property);
		} else
			return false;
	}

	public void setRange(RDFClass range) {
		this.range = range;
	}

	public void setDomain(RDFClass domain) {
		this.domain = domain;
	}
}
