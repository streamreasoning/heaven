package rdf.museo.ihneritance.generics.constrains.esper.events;

import rdf.museo.ihneritance.generics.constrains.ontology.properties.Paints;
import rdf.museo.ihneritance.generics.ontology.Paint;
import rdf.museo.ihneritance.generics.ontology.Painter;

public class PaintsEvent extends CreatesEvent<Painter, Paints, Paint> {

	public PaintsEvent(Painter s, Paint o, long ts) {
		super(s, new Paints(s.getSuper(), o.getSuper()), o, ts, "PaintsEvent");
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + getS() + ", p=" + getP() + ", o=" + getO() + "]";
	}

}
