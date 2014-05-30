package rdf.museo.events;

import rdf.museo.events.rdfs.RDFObject;
import rdf.museo.events.rdfs.RDFProperty;

public class RDFEvent<E, P extends RDFProperty<RDFObject>, T> {
	RDFObject s;
	RDFProperty<RDFObject> p;
	RDFObject c;

	public RDFEvent(RDFObject s, RDFProperty<RDFObject> p, RDFObject o) {
		this.s = s;
		this.p = p;
		this.c = o;
	}

	public RDFObject getS() {
		return s;
	}

	public void setS(RDFObject s) {
		this.s = s;
	}

	public RDFProperty getP() {
		return p;
	}

	public void setP(RDFProperty p) {
		this.p = p;
	}

	public RDFObject getC() {
		return c;
	}

	public void setC(RDFObject o) {
		this.c = o;
	}

	@Override
	public String toString() {
		return "RDFEvent [s=" + s + ", p=" + p + ", o=" + c + "]";
	}

}