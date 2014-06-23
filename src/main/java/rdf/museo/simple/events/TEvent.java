package rdf.museo.simple.events;

public class TEvent {
	String[] o, s;
	String p;
	long timestamp;
	String channel;

	public TEvent(String[] s, String p, String[] o, String ch, long ts) {
		this.s = s;
		this.p = p;
		this.o = o;
		this.channel = ch;
		this.timestamp = ts;
	}

	public String[] getS() {
		return s;
	}

	public void setS(String[] s) {
		this.s = s;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String[] getO() {
		return o;
	}

	public void setO(String[] c) {
		this.o = c;
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

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "TEvent [s=" + s.toString() + ", p=" + p + ", o=" + o.toString()
				+ "]";
	}

}