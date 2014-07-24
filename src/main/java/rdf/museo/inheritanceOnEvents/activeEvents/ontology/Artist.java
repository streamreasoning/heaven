package rdf.museo.inheritanceOnEvents.activeEvents.ontology;

import java.io.Serializable;

import rdf.museo.inheritanceOnEvents.activeEvents.rdfs.RDFResource;

public class Artist extends Person implements Serializable {

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
		return "Artist " + getName();
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
