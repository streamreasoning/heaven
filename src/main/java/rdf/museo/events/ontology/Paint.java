package rdf.museo.events.ontology;


public class Paint extends Piece {

	public Paint(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Paint " + getName();
	}

	@Override
	public Class<?> getType() {
		return Paint.class;
	}
}
