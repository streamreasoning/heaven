package it.polimi.processing.events.results;

import it.polimi.processing.events.TripleContainer;
import it.polimi.services.FileService;
import it.polimi.services.system.ExecutionEnvirorment;
import it.polimi.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j
public class TSResult implements EventResult {

	private String id;
	private OutputRDFStream result;
	private int eventNumber;
	private long inputTimestamp;
	private double memoryB;
	private double memoryA;
	private Boolean cs, ss, cr, sr;

	public int size() {
		return result.size();
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
		String compleAndSoundSIMPL = (cs != null && ss != null) ? "," + cs + "," + ss : "";
		String compleAndSoundRHODF = (cr != null && sr != null) ? "," + cr + "," + sr : "";

		String s = id + "," + eventNumber + "," + memoryB + "," + memoryA + "," + (result.getOutputTimestamp() - inputTimestamp)
				+ compleAndSoundSIMPL + compleAndSoundRHODF + System.getProperty("line.separator");
		return ExecutionEnvirorment.latencyLogEnabled || ExecutionEnvirorment.memoryLogEnabled ? FileService.write(FileUtils.getCSVpath(where), s)
				: false;
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
		for (TripleContainer tr : result.getEventTriples()) {
			String[] resource = tr.getTriple();
			trig += eol + "<" + resource[0] + ">" + " " + "<" + resource[1] + ">" + " " + "<" + resource[2] + "> .";
		}

		trig += eol + "}" + eol;
		return trig;
	}
}
