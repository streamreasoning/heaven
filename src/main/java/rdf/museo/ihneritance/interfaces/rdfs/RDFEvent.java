package rdf.museo.ihneritance.interfaces.rdfs;

public abstract class RDFEvent {
	private RDFResource s;
	private RDFProperty p;
	private RDFResource o;
	private long timestamp;
	private String channel;

	public RDFEvent(RDFResource s, RDFProperty p, RDFResource o, long ts,
			String channel) {
		this.s = s;
		this.p = p;
		this.o = o;
		this.timestamp = ts;
		this.channel = channel;
	}

	public RDFResource getS() {
		return s;
	}

	public void setS(RDFResource s) {
		this.s = s;
	}

	public RDFProperty getP() {
		return p;
	}

	public void setP(RDFProperty p) {
		this.p = p;
	}

	public RDFResource getO() {
		return o;
	}

	public void setO(RDFResource o) {
		this.o = o;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String from) {
		this.channel = from;
	}

	@Override
	public String toString() {
		return "RDFEvent " + channel + " [s=" + s + ", p=" + p + ", o=" + o
				+ " ts= " + timestamp + "]";
	}

}