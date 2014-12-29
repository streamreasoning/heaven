package it.polimi.processing.events.results;

import it.polimi.processing.events.TripleContainer;
import it.polimi.services.FileService;
import it.polimi.services.system.ExecutionEnvirorment;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j
public class Result implements EventResult {

	private String id;
	private Set<TripleContainer> statements;
	private long eventNumber;
	private long timestamp;
	private boolean abox;

	public int size() {
		return statements.size();
	}

	@Override
	public boolean saveTrig(String where) {
		log.debug("Save Data [" + ExecutionEnvirorment.finalresultTrigLogEnabled + "]");
		return ExecutionEnvirorment.finalresultTrigLogEnabled ? FileService.write(where, getData()) : !ExecutionEnvirorment.finalresultTrigLogEnabled;
	}

	@Override
	public boolean saveCSV(String where) {
		String s = "<http://example.org/" + eventNumber + ">" + System.getProperty("line.separator");
		return ExecutionEnvirorment.latencyLogEnabled || ExecutionEnvirorment.memoryLogEnabled ? FileService.write(where, s) : false;
	}

	private String getData() {

		String key;
		if (id == null) {
			key = "<http://example.org/" + eventNumber + ">";
		} else {
			key = this.id;
		}

		String eol = System.getProperty("line.separator");
		String trig = key + " {";
		for (TripleContainer tr : statements) {
			String[] resource = tr.getTriple();
			trig += eol + "<" + resource[0] + ">" + " " + "<" + resource[1] + ">" + " " + "<" + resource[2] + "> .";
		}

		trig += eol + "}" + eol;
		return trig;
	}

}
