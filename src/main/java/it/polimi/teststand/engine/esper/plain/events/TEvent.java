package it.polimi.teststand.engine.esper.plain.events;

import it.polimi.output.result.Storable;
import it.polimi.teststand.events.TestResultEvent;
import it.polimi.teststand.events.Experiment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TEvent  implements Storable{
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
	public TestResultEvent toEventResult(Experiment e) {
		return null;
	}

	@Override
	public Set<String[]> getTriples() {
		Set<String[]> triples = new HashSet<String[]>();
		for (int i = 0; i < s.length && i < o.length; i++) {
			triples.add(new String[] { s[i], p, o[i] });
		}
		return triples;
	}

}