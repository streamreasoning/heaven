package it.polimi.processing.events.results;

import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TripleContainer;
import it.polimi.services.FileService;
import it.polimi.services.system.ExecutionEnvirorment;
import it.polimi.utils.FileUtils;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Log4j
public class Result extends RSPTripleSet implements EventResult {

	private long outputTimestamp;
	private boolean abox;

	public Result(String id, Set<TripleContainer> hashSet, int eventNumber, int experimentNumber, long outputTimestamp, boolean abox) {
		super(id, hashSet, eventNumber, experimentNumber);
		this.outputTimestamp = outputTimestamp;
		this.abox = abox;
	}

	@Override
	public boolean save(String where) {
		log.debug("Save Data [" + ExecutionEnvirorment.finalresultTrigLogEnabled + "]");
		return saveTrig(where) && saveCSV(where);
	}

	public boolean saveTrig(String where) {
		log.debug("Save Data [" + ExecutionEnvirorment.finalresultTrigLogEnabled + "]");
		return ExecutionEnvirorment.finalresultTrigLogEnabled ? FileService.write(FileUtils.getTrigPath(where), getData())
				: !ExecutionEnvirorment.finalresultTrigLogEnabled;
	}

	public boolean saveCSV(String where) {
		String s = "<http://example.org/" + getEventNumber() + ">" + System.getProperty("line.separator");
		return ExecutionEnvirorment.latencyLogEnabled || ExecutionEnvirorment.memoryLogEnabled ? FileService.write(FileUtils.getCSVpath(where), s)
				: false;
	}

	private String getData() {

		String key;
		if (getId() == null) {
			key = "<http://example.org/" + getEventNumber() + ">";
		} else {
			key = getId();
		}

		String eol = System.getProperty("line.separator");
		String trig = key + " {";
		for (TripleContainer tr : getEventTriples()) {
			String[] resource = tr.getTriple();
			trig += eol + "<" + resource[0] + ">" + " " + "<" + resource[1] + ">" + " " + "<" + resource[2] + "> .";
		}

		trig += eol + "}" + eol;
		return trig;
	}

}
