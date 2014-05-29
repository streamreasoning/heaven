package rdf.museo.events.ontology;

import rdf.museo.events.rdfs.RDFObject;

public class Piece extends RDFObject {

	public Piece(String o) {
		super(o);
	}

	public String getName() {
		return object;
	}

	public void setName(String object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "Piece " + object;
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
		return Piece.class;
	}

}
