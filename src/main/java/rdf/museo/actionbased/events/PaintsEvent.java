package rdf.museo.actionbased.events;

import rdf.museo.ontology.Paint;
import rdf.museo.ontology.Painter;
import rdf.museo.ontology.properties.Paints;

public class PaintsEvent extends CreatesEvent<Painter, Paints, Paint> {

	public PaintsEvent(Painter s, Paints p, Paint o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
