package rdf.museo.ontology;

import rdf.museo.rdf.RDFObject;

public class Piece extends RDFObject {

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
		return getValue().equals(obj);
	}

	public Class<?> getType() {
		return Piece.class;
	}

}
