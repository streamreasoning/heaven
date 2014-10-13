package it.polimi.events.result;

import it.polimi.collector.Collectable;
import it.polimi.events.Event;
import it.polimi.events.StreamingEvent;

import java.util.Arrays;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StreamingEventResult extends Event implements Collectable {

	private StreamingEvent inputEvent;

	private Set<String[]> all_triples;

	private String result_event_id;
	private long result_timestamp;

	private String outputFileName;

	public StreamingEventResult(Set<String[]> all_triples,
			StreamingEvent inputEvent, String outputFileName) {
		this.inputEvent = inputEvent;
		this.result_timestamp = System.currentTimeMillis();
		// <http://example.org/StreamEventTS/ResultEventTS>
		this.result_event_id = "<http://example.org/"
				+ inputEvent.getTimestamp() + "/" + result_timestamp + ">";
		this.all_triples = all_triples;
		this.outputFileName = outputFileName;
	}

	/**
	 * <http://example.org/bob> { _:a foaf:name "Bob" . _:a foaf:mbox
	 * <mailto:bob@oldcorp.example.org> . _:a foaf:knows _:b . }
	 * 
	 * 
	 * **/
	@Override
	public String toString() {
		String eol = System.getProperty("line.separator");
		// TODO explain different in terms of keys
		String key = "[";
		for (String[] triple : inputEvent.getEventTriples()) {
			key += Arrays.deepToString(triple);

		}
		key += "]";

		String s = "<http://example.org/" + key.hashCode() + "> {";
		for (String[] resource : all_triples) {
			s += eol + "<" + resource[0] + ">" + "<" + resource[1] + ">" + "<"
					+ resource[2] + "> .";
		}

		s += eol + "}";
		return s;
	}

	public String getStartTripleEvent() {
		String starttriples = "[";
		for (String[] startTriple : inputEvent.getEventTriples()) {
			starttriples += Arrays.deepToString(startTriple);
		}
		starttriples += "]";
		return starttriples;
	}

	public int getLineNumber() {
		return inputEvent.getLineNumber();
	}

	public Set<String[]> getStartTriples() {
		return inputEvent.getEventTriples();
	}

	@Override
	public Set<String[]> getTriples() {
		return all_triples;
	}

	@Override
	public String getTrig() {
		String eol = System.getProperty("line.separator");
		// TODO explain different in terms of keys
		String key = "[";
		for (String[] triple : inputEvent.getEventTriples()) {
			key += Arrays.deepToString(triple);

		}
		key += "]";

		String s = "<http://example.org/" + key.hashCode() + "> {";
		for (String[] resource : all_triples) {
			s += eol + "<" + resource[0] + ">" + "<" + resource[1] + ">" + "<"
					+ resource[2] + "> .";
		}

		s += eol + "}";
		return s;
	}

	@Override
	public String getCSV() {
		long queryLatency = result_timestamp - inputEvent.getTimestamp();
		return inputEvent.getId() + "," + inputEvent.getTimestamp()
				+ ",Memory," + queryLatency;
	}

	@Override
	public String getSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes() {
		return toString().getBytes();
	}

	@Override
	public String getName() {
		return getOutputFileName();
	}

}
