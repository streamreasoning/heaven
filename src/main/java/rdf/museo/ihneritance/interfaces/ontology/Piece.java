package rdf.museo.ihneritance.interfaces.ontology;

import java.io.Serializable;

import rdf.museo.ihneritance.interfaces.rdfs.RDFClass;
import rdf.museo.ihneritance.interfaces.rdfs.RDFResource;

public class Piece extends RDFClass implements Serializable, RDFResource {

	private static final long serialVersionUID = 1L;

	public Piece(String name) {
		super(Piece.class, name);
	}

	public Piece(Class<? extends Piece> clazz, String name) {
		super(clazz, name);
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

}
