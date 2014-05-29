package rdf.museo.events.ontology;

public class Paints<Painter, Paint> extends Creates<Painter, Paint> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Paints(String property) {
		super(Painter.class, Paint.class, property);
	}

	public Paints() {
		this("paints");
	}

}
