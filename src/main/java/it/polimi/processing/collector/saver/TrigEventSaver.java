package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j;

@Log4j
public class TrigEventSaver implements EventSaver {

	private ExecutionStates status;
	private final String trigpath;
	private List<CollectableData> trigs;
	String path = "";

	public TrigEventSaver() {
		trigpath = FileUtils.TRIG_OUTPUT_FILE_PATH;
	}

	public TrigEventSaver(String path) {
		this.trigpath = path;
	}

	@Override
	public boolean save(CollectableData d, String where) {
		if (ExecutionStates.READY.equals(status)) {
			path = trigpath + where + FileUtils.TRIG_FILE_EXTENSION;
			trigs.add(d);
			log.debug("TRIG FILE PATH " + path);
			return true;
		} else {
			log.warn("Not Ready to register file");
			return false;
		}
	}

	@Override
	public ExecutionStates init() {
		log.info("Initialising TrigSaver... Nothing to do");
		trigs = new ArrayList<CollectableData>();
		status = ExecutionStates.READY;
		return status;
	}

	@Override
	public ExecutionStates close() {
		log.info("Closing TrigSaver... Nothing to do");
		String eof = System.getProperty("line.separator");
		try {
			File file;

			FileWriter writer = null;
			for (CollectableData d : trigs) {
				TriG t = (TriG) d;

				log.debug(path);
				file = new File(path);
				if (!file.exists()) {
					file.createNewFile();
				}
				writer = new FileWriter(file, true);
				writer.write(t.getKey() + " {");
				writer.write(eof);
				for (String[] resource : t.getTriples()) {
					writer.write(eof + "<" + resource[0] + ">" + " " + "<" + resource[1] + ">" + " " + "<" + resource[2] + "> .");
				}
				writer.write(eof + "}");
				writer.write(eof);
				writer.flush();
			}

			if (writer == null) {
				status = ExecutionStates.ERROR;
				log.error("Null Writer, File not Found");

			} else {
				writer.close();
				status = ExecutionStates.CLOSED;
			}

		} catch (IOException e) {
			log.error(e.getMessage());
			status = ExecutionStates.ERROR;
		}

		return status;
	}
}
