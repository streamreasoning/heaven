package rdf.museo.ontology;

public class Sculpt extends Piece {

	public Sculpt(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Sculpt " + getName();
	}

	@Override
	public Class<?> getType() {
		return Sculpt.class;
	}

}
