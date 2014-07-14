package rdf.museo.ihneritance.interfaces.rdfs;

public abstract class RDFTypeEvent {
	private RDFResource s;
	private RDFProperty p;
	private RDFClass o;
	private long timestamp;
	private String channel;

	public RDFTypeEvent(RDFResource s, RDFProperty p, RDFClass o, long ts,
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

	public RDFClass getO() {
		return o;
	}

	public void setO(RDFClass o) {
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