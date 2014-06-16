package rdf.museo.ihneritance.generics.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.generics.rdfs.RDFResource;

public class Sculptor extends Artist implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sculptor(String name) {
		super(name);
	}

	@Override
	public String toString() {

		return "Sculptor " + getName();
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

}
