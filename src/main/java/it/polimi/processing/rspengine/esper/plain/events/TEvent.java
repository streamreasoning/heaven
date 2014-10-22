package it.polimi.processing.rspengine.esper.plain.events;

import it.polimi.processing.rspengine.esper.TripleEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class TEvent implements TripleEvent {
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

	@Override
	public String toString() {
		return "TEvent [s=" + Arrays.deepToString(s) + ", p=" + p + ", o="
				+ Arrays.deepToString(o) + "ts=" + timestamp + "app_ts="
				+ app_timestamp + "]";
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