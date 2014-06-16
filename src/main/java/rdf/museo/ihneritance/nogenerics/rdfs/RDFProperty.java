package rdf.museo.ihneritance.nogenerics.rdfs;

import java.io.Serializable;

public class RDFProperty extends RDFResource implements Serializable {

	static final long serialVersionUID = 1L;
	protected RDFResource range;
	protected RDFResource domain;

	public RDFProperty(RDFResource domain, RDFResource range, String property) {
		super(property);
		this.range = range;
		this.domain = domain;
	}

	public RDFResource getRange() {
		return range;
	}

	public RDFResource getDomain() {
		return domain;

	}

	public void setRange(RDFResource range) {
		this.range = range;
	}

	public void setDomain(RDFResource domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		return getValue();
	}

	@Override
	public int hashCode() {
		return domain.hashCode() + range.hashCode() + getValue().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof RDFProperty) {
			RDFProperty other = (RDFProperty) obj;
			return other.getDomain().equals(domain)
					&& other.getRange().equals(range)
					&& other.getValue().equals(getValue());
		} else
			return false;
	}

}
