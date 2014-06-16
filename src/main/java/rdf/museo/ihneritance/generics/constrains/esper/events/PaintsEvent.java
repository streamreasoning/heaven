package rdf.museo.ihneritance.generics.constrains.esper.events;

import rdf.museo.ihneritance.generics.constrains.ontology.properties.PaintsObject;
import rdf.museo.ihneritance.generics.ontology.Paint;
import rdf.museo.ihneritance.generics.ontology.Painter;

public class PaintsEvent extends CreatesEvent<Painter, PaintsObject, Paint> {

	public PaintsEvent(Painter s, Paint o) {
		super(s, new PaintsObject(s.getRDFClass(), o.getRDFClass()), o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
