package rdf.museo.ontology;

import rdf.museo.rdf.RDFObject;

public class Artist extends RDFObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Artist(String name) {
		super(name);
	}

	public String getName() {
		return getValue();
	}

	public void setName(String name) {
		setValue(name);
	}

	@Override
	public String toString() {
		return "Artist " + getValue();
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (!(obj instanceof RDFObject))
			return false;
		else {
			RDFObject other = (RDFObject) obj;
			return getValue().equals(other.getValue());
		}
	}

}
