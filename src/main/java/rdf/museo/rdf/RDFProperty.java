package rdf.museo.rdf;

import java.io.Serializable;

public abstract class RDFProperty<RDFObject> implements Serializable {

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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result
				+ ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RDFProperty other = (RDFProperty) obj;
		return other.getDomain().equals(domain)
				&& other.getRange().equals(range)
				&& other.property.equals(property);
	}

	public void setRange(RDFClass range) {
		this.range = range;
	}

	public void setDomain(RDFClass domain) {
		this.domain = domain;
	}
}
