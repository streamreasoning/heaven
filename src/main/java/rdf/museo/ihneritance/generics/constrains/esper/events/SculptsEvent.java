package rdf.museo.ihneritance.generics.constrains.esper.events;

import rdf.museo.ihneritance.generics.constrains.ontology.properties.Sculpts;
import rdf.museo.ihneritance.generics.ontology.Sculpt;
import rdf.museo.ihneritance.generics.ontology.Sculptor;

public class SculptsEvent extends CreatesEvent<Sculptor, Sculpts, Sculpt> {

	public SculptsEvent(Sculptor s, Sculpt o, long ts) {
		super(s, new Sculpts(s.getSuper(), o.getSuper()), o, ts, "SculptsEvent");
	}

	@Override
	public String toString() {
		return "SculptsEvent [s=" + getS() + ", p=" + getP() + ", o=" + getO()
				+ "]";
	}

}
