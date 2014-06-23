package rdf.museo.ihneritance.nogenerics.rdfs;

import java.io.Serializable;

public class RDFProperty implements Serializable {

	static final long serialVersionUID = 1L;
	protected String property;
	protected Class<? extends RDFResource> range;
	protected Class<? extends RDFResource> domain;

	public RDFProperty(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		this.range = range;
		this.domain = domain;
		this.property = property;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public RDFClass getRange() {
		return new RDFClass(range);
	}

	public void setRange(Class<? extends RDFResource> range) {
		this.range = range;
	}

	public RDFClass getDomain() {
		return new RDFClass(domain);
	}

	public void setDomain(Class<? extends RDFResource> domain) {
		this.domain = domain;
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
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RDFProperty " + property;
	}

}
