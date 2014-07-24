package rdf.museo.inheritanceOnEvents.activeEvents.rdfs;

import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Artist;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.Piece;
import rdf.museo.inheritanceOnEvents.activeEvents.ontology.properties.Creates;

public abstract class RDFEvent<S extends RDFResource, P extends RDFProperty, O extends RDFResource> {
	private S s;
	private P p;
	private O o;
	private long timestamp;
	private String channel;

	public RDFEvent(S s, P p, O o, long ts, String ch) {
		this.s = s;
		this.p = p;
		this.o = o;
		this.setTimestamp(ts);
		this.setChannel(ch);
	}

	public RDFEvent(RDFEvent<S, P, O> e) {
		this.s = e.s;
		this.p = e.p;
		this.o = e.o;
		this.timestamp = e.timestamp;
		this.channel = e.channel;
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

	public O getO() {
		return o;
	}

	public void setO(O o) {
		this.o = o;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "RDFEvent " + channel + " [s=" + s + ", p=" + p + ", o=" + o
				+ " ts= " + timestamp + "]";
	}

	public RDFEvent<S, P, O> getCurrent() {
		return this;

	}

	public abstract RDFEvent<? extends Artist, ? extends Creates, ? extends Piece> getSuperEvent();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((o == null) ? 0 : o.hashCode());
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RDFEvent other = (RDFEvent) obj;
		if (o == null) {
			if (other.o != null)
				return false;
		} else if (!o.equals(other.o))
			return false;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

}