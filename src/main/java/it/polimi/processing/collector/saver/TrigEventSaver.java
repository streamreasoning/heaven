package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.collector.saver.data.TriG;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import lombok.extern.log4j.Log4j;

@Log4j
public class TrigEventSaver implements EventSaver {

	private ExecutionState status;
	private final String trigpath;
	String path = "";

	public TrigEventSaver() {
		trigpath = FileUtils.TRIG_OUTPUT_FILE_PATH;
	}

	public TrigEventSaver(String path) {
		this.trigpath = path;
	}

	@Override
	public boolean save(CollectableData d, String where) {
		if (ExecutionState.READY.equals(status)) {
			path = trigpath + where + FileUtils.TRIG_FILE_EXTENSION;
			try {
				FileWriter writer;
				File file = new File(path);
				TriG t = (TriG) d;
				if (!file.exists()) {
					file.createNewFile();
				}
				writer = new FileWriter(file, true);
				writer.write(t.getData());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				log.error(e.getMessage());
				status = ExecutionState.ERROR;
				return false;
			}
			log.debug("TRIG FILE PATH " + path);
			return true;
		} else {
			log.warn("Not Ready to register file");
			return false;
		}
	}

	@Override
	public ExecutionState init() {
		log.info("Initialising TrigSaver... Nothing to do");
		status = ExecutionState.READY;
		return status;
	}

	@Override
	public ExecutionState close() {
		log.info("Closing TrigSaver... Nothing to do");
		status = ExecutionState.CLOSED;
		return status;
	}

}
