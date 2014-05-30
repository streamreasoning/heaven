package rdf.museo.actionbased.events;

import rdf.RDFEvent;
import rdf.museo.ontology.Sculpt;
import rdf.museo.ontology.Sculptor;
import rdf.museo.ontology.properties.Sculpts;
import rdf.museo.rdf.RDFClass;
import rdf.museo.rdf.RDFObject;
import rdf.museo.rdf.TypeOf;

public class TypeOfEvent extends RDFEvent<RDFObject, TypeOf, RDFClass> {

	public TypeOfEvent(Sculptor s, Sculpts p, Sculpt o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "RDF3 [s=" + getS() + ", p=" + getP() + ", o=" + getC() + "]";
	}

}
