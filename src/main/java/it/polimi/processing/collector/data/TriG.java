package it.polimi.processing.collector.data;

import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.streamer.Parser;
import it.polimi.services.system.ExecutionEnvirorment;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Log4j
public final class TriG extends WritableData {

	private String key;
	private Set<TripleContainer> triples;

	@Override
	public String getData() {
		// TODO is not good to be write in file, maybe a accessible method that mantains the logic?
		// self savign data?
		String eol = System.getProperty("line.separator");
		String trig = key + " {";
		for (TripleContainer tr : triples) {
			String[] resource = tr.getTriple();
			trig += eol + "<" + resource[0] + ">" + " " + "<" + resource[1] + ">" + " " + "<" + resource[2] + "> .";
		}

		trig += eol + "}" + eol;
		return trig;
	}

	@Override
	public CollectableData append(String triple) {
		Set<TripleContainer> triples2 = getTriples();
		triples.add(new TripleContainer(Parser.parseTriple(triple)));
		return new TriG(key, triples2);
	}

	@Override
	public boolean save(String where) {
		log.debug("Save Data [" + ExecutionEnvirorment.finalresultTrigLogEnabled + "]");
		return ExecutionEnvirorment.finalresultTrigLogEnabled ? super.save(where) : !ExecutionEnvirorment.finalresultTrigLogEnabled;
	}

}
