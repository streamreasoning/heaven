package rdf;

import rdf.museo.rdf.RDFObject;
import rdf.museo.rdf.RDFProperty;

public class RDFEvent<E, P extends RDFProperty<RDFObject>, T> {
	private RDFObject s;
	private RDFProperty<RDFObject> p;
	private RDFObject c;

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

	public RDFProperty<RDFObject> getP() {
		return p;
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

	public void setP(RDFProperty<RDFObject> p) {
		this.p = p;
	}

}