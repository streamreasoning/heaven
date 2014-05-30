package rdf.museo.events.ontology;

import rdf.museo.events.rdfs.RDFObject;

public class Painter extends Artist {

	public Painter(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Painter " + getName();
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
