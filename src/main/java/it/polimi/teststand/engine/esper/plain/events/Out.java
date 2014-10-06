package it.polimi.teststand.engine.esper.plain.events;

import it.polimi.output.result.Storable;
import it.polimi.teststand.events.TestResultEvent;
import it.polimi.teststand.events.Experiment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Out implements Storable {
	private String[] o, s;
	private String p, channel;
	private long timestamp, app_timestamp;

	public Out() {

	}

	public Out(String[] s, String p, String[] o, String ch, long timestamp,
			long app_timestamp) {
		this.s = s;
		this.p = p;
		this.o = o;
		this.channel = ch;
		this.app_timestamp = app_timestamp;
		this.timestamp = timestamp;
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
		return "Out [o=" + Arrays.toString(o) + ", s=" + Arrays.toString(s)
				+ ", p=" + p + ", timestamp=" + timestamp + ", app_timestamp="
				+ app_timestamp + ", channel=" + channel + "]";
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

	public long getApp_timestamp() {
		return app_timestamp;
	}

	public void setApp_timestamp(long app_timestamp) {
		this.app_timestamp = app_timestamp;
	}

}