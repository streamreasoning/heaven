package rdf.museo.querybased.events;

import rdf.RDFEvent;
import rdf.museo.rdf.RDFObject;
import rdf.museo.rdf.RDFProperty;

public class ThreeTest extends
		RDFEvent<RDFObject, RDFProperty<RDFObject>, RDFObject> {

	public ThreeTest(RDFObject s, RDFProperty<RDFObject> p, RDFObject o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "threetest [s=" + getS() + ", p=" + getP() + ", o=" + getC()
				+ "]";
	}

}
