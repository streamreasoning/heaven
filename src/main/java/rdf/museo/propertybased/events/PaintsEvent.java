package rdf.museo.propertybased.events;

import rdf.museo.ontology.Paint;
import rdf.museo.ontology.Painter;
import rdf.museo.ontology.properties.objectbased.PaintsObject;

public class PaintsEvent extends CreatesEvent<Painter, PaintsObject, Paint> {

	public PaintsEvent(Painter s, Paint o) {
		super(s, new PaintsObject(s.getRDFClass(), o.getRDFClass()), o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
