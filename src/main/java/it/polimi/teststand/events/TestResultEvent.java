package it.polimi.teststand.events;

import it.polimi.events.ResultEvent;
import it.polimi.output.filesystem.Writable;

import java.util.Arrays;
import java.util.Set;

public class TestResultEvent extends ResultEvent implements Writable {

	private Set<String[]> all_triples;
	private Set<String[]> start_triples;

	private String event_id, outputFileName, folder;
	private long result_timestamp;
	private long event_timestamp;
	private long experiment_timestamp;

	public TestResultEvent(Set<String[]> all_triples,
			Set<String[]> start_triples, long event_timestamp,
			String outputFileName, String folder, long experiment_timestamp,
			int lineNumber) {
		this.folder = folder;
		this.result_timestamp = System.currentTimeMillis();
		this.event_timestamp = event_timestamp;
		this.experiment_timestamp = experiment_timestamp;
		// TODO event final name
		// <http://example.org/StreamEventTS/ResultEventTS>
		this.event_id = "<http://example.org/line=" + lineNumber + "/"
				+ event_timestamp + "/" + result_timestamp + ">";

		this.outputFileName = outputFileName;
		this.all_triples = all_triples;
		this.start_triples = start_triples;
	}

	@Override
	/**
	 * <http://example.org/bob>
	{
	   _:a foaf:name "Bob" .
	   _:a foaf:mbox <mailto:bob@oldcorp.example.org> .
	   _:a foaf:knows _:b .
	}
	 * 
	 * 
	 * **/
	public String toString() {
		String eol = System.getProperty("line.separator");
		// TODO explain different in terms of keys
		String key = "[";
		for (String[] triple : start_triples) {
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

	public long getEvent_timestamp() {
		return event_timestamp;
	}

	@Override
	public byte[] getBytes() {
		return toString().getBytes();
	}

	public String getEvent_id() {
		return event_id;
	}

	public long getResultTimestamp() {
		return result_timestamp;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public long getExperiment_timestamp() {
		return experiment_timestamp;
	}

	public String getFolder() {
		return folder + "/";
	}

	public Set<String[]> getStart_triples() {
		return start_triples;
	}

	public void setStart_triples(Set<String[]> start_triples) {
		this.start_triples = start_triples;
	}

	public String getStartTripleEvent() {
		String starttriples = "[";
		for (String[] startTriple : start_triples) {
			starttriples += Arrays.deepToString(startTriple);
		}
		starttriples += "]";
		return starttriples;
	}

}
