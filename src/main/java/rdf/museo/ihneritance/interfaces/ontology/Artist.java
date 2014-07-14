package rdf.museo.ihneritance.interfaces.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.interfaces.rdfs.RDFResource;

public class Artist extends Person implements Serializable, RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Artist(String name) {
		super(Artist.class, name);
	}

	public Artist(Class<? extends Artist> clazz, String name) {
		super(clazz, name);
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

}
