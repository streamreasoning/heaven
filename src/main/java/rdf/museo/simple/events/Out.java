package rdf.museo.simple.events;

public class Out {
	String[] o, s;
	String p;
	long timestamp;
	String channel;

	public Out(String[] s, String p, String[] o, String ch, long ts) {
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
		return "Out s= [ " + print(s) + "], p=" + p + ", o= [" + print(o)
				+ "], channel= " + channel + "";
	}

	private String print(String[] s2) {
		String r = "";
		for (String string : s2) {
			r = r + string + ",";

		}
		return r;
	}
}