package rdf.museo.ihneritance.generics.rdfs;

import java.io.Serializable;

public class RDFProperty<D extends RDFResource, R extends RDFResource> extends
		RDFResource implements Serializable {

	/**
	 * 
	 */
	static final long serialVersionUID = 1L;
	protected R range;
	protected D domain;

	public RDFProperty(D domain, R range, String property) {
		super(property);
		this.range = range;
		this.domain = domain;
	}

	public R getRange() {
		return range;
	}

	public D getDomain() {
		return domain;

	}

	public void setRange(R range) {
		this.range = range;
	}

	public void setDomain(D domain) {
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
			@SuppressWarnings("unchecked")
			RDFProperty<R, D> other = (RDFProperty<R, D>) obj;
			return other.getDomain().equals(domain)
					&& other.getRange().equals(range)
					&& other.getValue().equals(getValue());
		} else
			return false;
	}

}
