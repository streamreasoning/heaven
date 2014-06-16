package rdf.museo.ihneritance.generics.constrains.esper.events;

import rdf.museo.ihneritance.generics.constrains.ontology.properties.SculptsObject;
import rdf.museo.ihneritance.generics.ontology.Sculpt;
import rdf.museo.ihneritance.generics.ontology.Sculptor;

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
