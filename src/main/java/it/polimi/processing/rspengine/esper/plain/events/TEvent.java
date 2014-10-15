package it.polimi.processing.rspengine.esper.plain.events;

import it.polimi.processing.collector.Collectable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TEvent  implements Collectable{
	String[] o, s;
	String p;
	long timestamp, app_timestamp;
	String channel;

	public long getApp_timestamp() {
		return app_timestamp;
	}

	public void setApp_timestamp(long app_timestamp) {
		this.app_timestamp = app_timestamp;
	}

	public TEvent(String[] s, String p, String[] o, String ch, long ts) {
		this.s = s;
		this.p = p;
		this.o = o;
		this.channel = ch;
		this.timestamp = ts;
		this.app_timestamp = System.currentTimeMillis();
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
		return "TEvent [s=" + Arrays.deepToString(s) + ", p=" + p + ", o=" +  Arrays.deepToString(o)
				+ "ts="+timestamp+ "app_ts="+app_timestamp+"]";
	}


	@Override
	public Set<String[]> getTriples() {
		Set<String[]> triples = new HashSet<String[]>();
		for (int i = 0; i < s.length && i < o.length; i++) {
			triples.add(new String[] { s[i], p, o[i] });
		}
		return triples;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTrig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCSV() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}

}