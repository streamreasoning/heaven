package it.polimi.processing.collector.saver.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TriG implements CollectableData {
	private String key;
	private List<String[]> triples;

	@Override
	public String getData() {

		String eol = System.getProperty("line.separator");
		String trig = key + " {";
		for (String[] resource : triples) {
			trig += eol + "<" + resource[0] + ">" + " " + "<" + resource[1] + ">" + " " + "<" + resource[2] + "> .";
		}

		trig += eol + "}";
		return trig;
	}

	public TriG(String event_key, Set<String[]> triples) {
		this(event_key, new ArrayList<String[]>(triples));
	}

}
