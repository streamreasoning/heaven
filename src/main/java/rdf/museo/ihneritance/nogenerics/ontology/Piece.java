package rdf.museo.ihneritance.nogenerics.ontology;

import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

public class Piece extends RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Piece(String o) {
		super(o);
	}

	public String getName() {
		return getValue();
	}

	public void setName(String object) {
		this.setValue(object);
	}

	@Override
	public String toString() {
		return "Piece " + getValue();
	}

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

	@Override
	public RDFClass getSuper() {
		return new RDFClass(this.getClass().getSuperclass());
	}

}
