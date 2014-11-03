package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lombok.extern.log4j.Log4j;

@Log4j
public class TrigEventSaver implements EventSaver {

	private ExecutionStates status;
	private final String trigpath;

	public TrigEventSaver() {
		trigpath = FileUtils.TRIG_OUTPUT_FILE_PATH;
	}

	public TrigEventSaver(String path) {
		this.trigpath = path;
	}

	@Override
	public boolean save(CollectableData d, String where) {
		try {
			if (ExecutionStates.READY.equals(status)) {
				String path = trigpath + where + FileUtils.TRIG_FILE_EXTENSION;
				log.debug("TRIG FILE PATH " + path);
				File file = new File(path);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fop = new FileOutputStream(file, true);
				fop.write(d.getData().getBytes());
				fop.write(System.getProperty("line.separator").getBytes());
				fop.flush();
				fop.close();
				return true;
			} else {
				log.warn("Not Ready to write file");
				return false;
			}
		} catch (IOException e1) {
			log.error(e1.getMessage());
			return false;
		}
	}

	@Override
	public ExecutionStates init() {
		log.info("Initialising TrigSaver... Nothing to do");
		status = ExecutionStates.READY;
		return status;
	}

	@Override
	public ExecutionStates close() {
		log.info("Closing TrigSaver... Nothing to do");
		status = ExecutionStates.CLOSED;
		return status;
	}

}
