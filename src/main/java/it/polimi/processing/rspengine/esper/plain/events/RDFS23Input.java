package it.polimi.processing.rspengine.esper.plain.events;

import it.polimi.processing.rspengine.esper.TripleEvent;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RDFS23Input implements TripleEvent {
	String s, p, o;
	long timestamp, app_timestamp;
	String channel;

	@Override
	public String toString() {
		return "TEvent [s=" + s + ", p=" + p + ", o=" + o + "ts=" + timestamp + "app_ts=" + app_timestamp + "]";
	}

	@Override
	public Set<String[]> getTriples() {
		String[] strings = new String[] { s, p, o };
		Set<String[]> hashSet = new HashSet<String[]>();
		hashSet.add(strings);
		return hashSet;
	}

	public String[] getSs() {
		return new String[] { s };
	}

	public String[] getPs() {
		return new String[] { p };
	}

	public String[] getOs() {
		return new String[] { o };
	}
}