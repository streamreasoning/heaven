package rdf.museo.events.ontology;

public class Sculpts extends Creates {

	public Sculpts(String property) {
		super(Sculptor.class, Sculpt.class, property);
	}

	public Sculpts() {
		this("scuplts");
	}
}
