package it.polimi.heaven.core.ts.events;

import it.polimi.services.FileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j
public class HeavenResult implements EventResult {

	private String id;
	private RSPEngineResult result;
	private int eventNumber;
	private long inputTimestamp;
	private double memoryB;
	private double memoryA;
	private Boolean cs, ss, cr, sr;
	private boolean memoryLog, latencyLog, resultLog, aboxLog;

	public int size() {
		return result.size();
	}

	@Override
	public boolean save(String where) {
		log.debug("Save Data [" + resultLog + "]");

		return saveCSV(where) && saveTrig(where);

	}

	public boolean saveTrig(String where) {
		log.debug("saveTrig: Data [" + resultLog + "]");

		return resultLog ? FileService.write(where + ".trig", getData()) : !resultLog;
	}

	public boolean saveCSV(String where) {
		String compleAndSoundSIMPL = (cs != null && ss != null) ? "," + cs + "," + ss : "";
		String compleAndSoundRHODF = (cr != null && sr != null) ? "," + cr + "," + sr : "";

		String s = id + "," + eventNumber + "," + memoryB + "," + memoryA + "," + (result.getOutputTimestamp() - inputTimestamp)
				+ compleAndSoundSIMPL + compleAndSoundRHODF + System.getProperty("line.separator");
		log.info(s);
		return latencyLog || memoryLog ? FileService.write(where + ".csv", s) : false;
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

	public long getOutputTimestamp() {
		return result.getOutputTimestamp();
	}

	public long getLatency() {
		return result.getOutputTimestamp() - inputTimestamp;
	}

}
