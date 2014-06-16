package rdf.museo.ihneritance.generics.rdfs;

public abstract class RDFEvent<S extends RDFResource, P extends RDFProperty<?, ?>, O extends RDFResource> {
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

}