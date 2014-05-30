package rdf.museo.querybased.events;

import rdf.RDFEvent;
import rdf.museo.rdf.RDFObject;
import rdf.museo.rdf.RDFProperty;

public class NineTest extends
		RDFEvent<RDFObject, RDFProperty<RDFObject>, RDFObject> {

	public NineTest(RDFObject s, RDFProperty<RDFObject> p, RDFObject o) {
		super(s, p, o);
	}

	@Override
	public String toString() {
		return "Ninetest [s=" + getS() + ", p=" + getP() + ", o=" + getC()
				+ "]";
	}

}
