package it.polimi.processing.collector.saver.data;

import it.polimi.processing.streamer.Parser;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TriG implements CollectableData {
	private String key;
	private Set<String[]> triples;

	@Override
	public String getData() {
		// TODO is not good to be write in file, maybe a accessible method that mantains the logic?
		// self savign data?
		String eol = System.getProperty("line.separator");
		String trig = key + " {";
		for (String[] resource : triples) {
			trig += eol + "<" + resource[0] + ">" + " " + "<" + resource[1] + ">" + " " + "<" + resource[2] + "> .";
		}

		trig += eol + "}" + eol;
		return trig;
	}

	@Override
	public CollectableData append(String triple) {
		Set<String[]> triples2 = getTriples();
		triples.add(Parser.parseTriple(triple));
		return new TriG(key, triples2);
	}
}
