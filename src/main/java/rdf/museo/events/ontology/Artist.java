package rdf.museo.events.ontology;

import rdf.museo.events.rdfs.RDFObject;

public class Artist extends RDFObject {

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

	public Class<?> getType() {
		if (this.getClass().getSuperclass().equals(Object.class))
			return this.getClass();
		else
			return this.getClass().getSuperclass();
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
