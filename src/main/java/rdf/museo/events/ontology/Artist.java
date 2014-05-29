package rdf.museo.events.ontology;

import rdf.museo.events.rdfs.RDFObject;

public class Artist extends RDFObject {

	public Artist(String name) {
		super(name);
	}

	public String getName() {
		return object;
	}

	public void setName(String name) {
		object = name;
	}

	@Override
	public String toString() {
		return "Artist " + object;
	}

	@Override
	public int hashCode() {
		return object.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return object.equals(obj);
	}

	public Class<?> getType() {
		if (this.getClass().getSuperclass().equals(Object.class))
			return this.getClass();
		else
			return this.getClass().getSuperclass();
	}

}
