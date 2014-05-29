package rdf.museo.events;

import rdf.museo.events.rdfs.RDFProperty;

public class RDFS9Input<E, P extends RDFProperty<E, T>, T> {
	E s;
	RDFProperty<E, T> p;
	T c;

	public RDFS9Input(E s, RDFProperty<E, T> p, T o) {
		this.s = s;
		this.p = p;
		this.c = o;
	}

	public E getS() {
		return s;
	}

	public void setS(E s) {
		this.s = s;
	}

	public RDFProperty getP() {
		return p;
	}

	public void setP(RDFProperty p) {
		this.p = p;
	}

	public T getC() {
		return c;
	}

	public void setC(T o) {
		this.c = o;
	}

	public Class<?> getSType() {
		return s.getClass();
	}

	public Class<?> getOType() {
		return c.getClass();
	}

	@Override
	public String toString() {
		return "RDFEvent [s=" + s + ", p=" + p + ", o=" + c + "]";
	}

}