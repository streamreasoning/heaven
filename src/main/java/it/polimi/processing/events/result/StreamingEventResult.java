package it.polimi.processing.events.result;

import it.polimi.processing.collector.saver.data.CSV;
import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.StreamingEvent;

import java.util.Arrays;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;

@Data
@EqualsAndHashCode(callSuper = false)
public class StreamingEventResult extends Event {

	private StreamingEvent inputEvent;
	private Set<String[]> all_triples;
	private String result_event_id, outputFileName;
	private long result_timestamp;

	public StreamingEventResult(Set<String[]> all_triples,
			StreamingEvent inputEvent, String outputFileName) {
		this.inputEvent = inputEvent;
		this.result_timestamp = System.currentTimeMillis();
		this.result_event_id = "<http://example.org/resutof/"
				+ inputEvent.getEventNumber() + ">";
		this.all_triples = all_triples;
		this.outputFileName = outputFileName;
	}

	public String getStartTripleEvent() {
		String starttriples = "[";
		for (String[] startTriple : inputEvent.getEventTriples()) {
			starttriples += Arrays.deepToString(startTriple);
		}
		starttriples += "]";
		return starttriples;
	}

	public int[] getLineNumbers() {
		return inputEvent.getLineNumbers();
	}

	public Set<String[]> getStartTriples() {
		return inputEvent.getEventTriples();
	}

	public Set<String[]> getTriples() {
		return all_triples;
	}

	/**
	 * <http://example.org/bob> { _:a foaf:name "Bob" . _:a foaf:mbox
	 * <mailto:bob@oldcorp.example.org> . _:a foaf:knows _:b . }
	 * 
	 * 
	 * **/
	public TriG getTrig() {
		String eol = System.getProperty("line.separator");
		Logger.getLogger("obqa").debug(
				"Event KEY :" + inputEvent.getEventNumber());

		String s = inputEvent.getId() + " {";
		for (String[] resource : all_triples) {
			s += eol + "<" + resource[0] + ">" + "<" + resource[1] + ">" + "<"
					+ resource[2] + "> .";
		}

		s += eol + "}";
		return new TriG(s, inputEvent.getEngine() + "/" + outputFileName);
	}

	public CSV getCSV() {
		String lines = ",";
		for (int p : inputEvent.getLineNumbers()) {
			lines += p + ",";
		}

		long queryLatency = result_timestamp - inputEvent.getTimestamp();
		String s = inputEvent.getId() + lines + inputEvent.getTimestamp()
				+ ",0," + queryLatency;
		return new CSV(s, inputEvent.getEngine() + "/" + outputFileName);
	}

}
