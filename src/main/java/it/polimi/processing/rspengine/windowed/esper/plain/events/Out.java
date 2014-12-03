package it.polimi.processing.rspengine.windowed.esper.plain.events;

import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.rspengine.windowed.esper.TripleEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class Out implements TripleEvent {
	private String[] s, p, o;
	private String channel;
	private long timestamp, app_timestamp;

	public Out() {

	}

	public Out(String[] s, String[] p, String[] o, String ch, long timestamp, long app_timestamp) {
		this.s = s;
		this.p = p;
		this.o = o;
		this.channel = ch;
		this.app_timestamp = app_timestamp;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Out [o=" + Arrays.toString(o) + ", s=" + Arrays.toString(s) + ", p=" + Arrays.toString(p) + ", timestamp=" + timestamp
				+ ", app_timestamp=" + app_timestamp + ", channel=" + channel + "]";
	}

	@Override
	public Set<TripleContainer> getTriples() {
		Set<TripleContainer> triples = new HashSet<TripleContainer>();
		List<String> list;

		for (String subj : s) {
			list = new ArrayList<String>();
			for (String obj : o) {
				for (String prop : p) {
					list.add(subj + "," + prop + "," + obj);
				}
			}

			for (String string : list) {
				triples.add(new TripleContainer(string.split(",")));
			}
		}

		return triples;
	}

}