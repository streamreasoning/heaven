package rdf.museo.propertybased.events;

import rdf.museo.ontology.Sculpt;
import rdf.museo.ontology.Sculptor;
import rdf.museo.ontology.properties.objectbased.SculptsObject;

public class SculptsEvent extends CreatesEvent<Sculptor, SculptsObject, Sculpt> {

	public SculptsEvent(Sculptor s, Sculpt o) {
		super(s, new SculptsObject(s.getRDFClass(), o.getRDFClass()), o);
	}

	@Override
	public String toString() {
		return "SculptsEvent [s=" + getS() + ", p=" + getP() + ", o=" + getC()
				+ "]";
	}

}
