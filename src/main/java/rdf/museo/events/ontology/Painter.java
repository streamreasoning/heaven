package rdf.museo.events.ontology;

public class Painter extends Artist {

	public Painter(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Painter " + getName();
	}

}
