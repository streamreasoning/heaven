package rdf.museo.events.ontology;

public class Sculptor extends Artist {

	public Sculptor(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Sculptor " + getName();
	}

}
