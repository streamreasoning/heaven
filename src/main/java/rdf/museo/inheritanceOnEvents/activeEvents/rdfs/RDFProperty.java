package rdf.museo.inheritanceOnEvents.activeEvents.rdfs;

import java.io.Serializable;

public abstract class RDFProperty implements Serializable {

	/**
	 * 
	 */
	static final long serialVersionUID = 1L;
	protected Class<? extends RDFResource> range;
	protected Class<? extends RDFResource> domain;
	protected String property;

	public RDFProperty(Class<? extends RDFResource> domain,
			Class<? extends RDFResource> range, String property) {
		this.property = property;
		this.range = range;
		this.domain = domain;
	}

	public RDFClass<? extends RDFResource> getRange() {
		return new RDFClass(range);
	}

	public RDFClass<? extends RDFResource> getDomain() {
		return new RDFClass(domain);

	}

	public void setRange(Class<? extends RDFResource> range) {
		this.range = range;
	}

	public void setDomain(Class<? extends RDFResource> domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		return property; // + " DOMAIN: " + domain.getSimpleName() + " RANGE: "+
							// range.getSimpleName();

	}

	@Override
	public int hashCode() {
		return domain.hashCode() + range.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof RDFProperty) {
			RDFProperty other = (RDFProperty) obj;
			return other.getDomain().equals(domain)
					&& other.getRange().equals(range)
					&& property.equals(property);
		} else
			return false;
	}

}
