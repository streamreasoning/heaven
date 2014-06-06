package rdf;

import rdf.museo.rdf.RDFProperty;
import rdf.museo.rdf.RDFResource;

public class RDFEvent<S extends RDFResource, P extends RDFProperty<?, ?>, O extends RDFResource> {
	private S s;
	private P p;
	private O c;

	public RDFEvent(S s, P p, O o) {
		this.s = s;
		this.p = p;
		this.c = o;
	}

	public S getS() {
		return s;
	}

	public void setS(S s) {
		this.s = s;
	}

	public P getP() {
		return p;
	}

	public void setP(P p) {
		this.p = p;
	}

	public O getC() {
		return c;
	}

	public void setC(O o) {
		this.c = o;
	}

	@Override
	public String toString() {
		return "RDFEvent [s=" + s + ", p=" + p + ", o=" + c + "]";
	}

}