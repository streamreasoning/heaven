package it.polimi.processing.rspengine.esper.plain.events;

import it.polimi.processing.rspengine.esper.TripleEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class Out implements TripleEvent {
	private String[] o, s;
	private String p, channel;
	private long timestamp, app_timestamp;

	public Out() {

	}

	public Out(String[] s, String p, String[] o, String ch, long timestamp, long app_timestamp) {
		this.s = s;
		this.p = p;
		this.o = o;
		this.channel = ch;
		this.app_timestamp = app_timestamp;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Out [o=" + Arrays.toString(o) + ", s=" + Arrays.toString(s) + ", p=" + p + ", timestamp=" + timestamp + ", app_timestamp="
				+ app_timestamp + ", channel=" + channel + "]";
	}

	@Override
	public Set<String[]> getTriples() {
		Set<String[]> triples = new HashSet<String[]>();
		List<String> list;

		for (String subj : s) {
			list = new ArrayList<String>();
			for (String obj : o) {
				list.add(subj + "," + p + "," + obj);
			}

			for (String string : list) {
				triples.add(string.split(","));
			}
		}

		return triples;
	}

}