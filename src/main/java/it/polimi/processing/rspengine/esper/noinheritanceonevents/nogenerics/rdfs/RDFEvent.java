package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs;

import it.polimi.processing.rspengine.esper.TripleEvent;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class RDFEvent implements TripleEvent {
	private RDFResource s;
	private RDFProperty p;
	private RDFResource o;
	private long timestamp;
	private String channel;

	@Override
	public String toString() {
		return "RDFEvent " + channel + " [s=" + s + ", p=" + p + ", o=" + o + " ts= " + timestamp + "]";
	}

	@Override
	public Set<String[]> getTriples() {
		Set<String[]> set = new HashSet<String[]>();
		String[] triple = new String[] { s + "", p + "", o + "" };
		set.add(triple);
		return set;

	}
}