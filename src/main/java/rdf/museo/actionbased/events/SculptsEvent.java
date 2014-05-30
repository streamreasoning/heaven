package rdf.museo.actionbased.events;

import rdf.museo.ontology.Sculpt;
import rdf.museo.ontology.Sculptor;
import rdf.museo.ontology.Sculpts;

public class SculptsEvent extends CreatesEvent<Sculptor, Sculpts, Sculpt> {

	public SculptsEvent(Sculptor s, Sculpts p, Sculpt o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
