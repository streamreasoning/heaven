package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import lombok.extern.log4j.Log4j;

@Log4j
public class TXTEventSaver implements EventSaver {

	private ExecutionStates status;
	Date d = new Date();

	@Override
	public ExecutionStates init() {
		log.info("Initialising CSVSaver... Nothing to do");
		status = ExecutionStates.READY;
		return status;
	}

	@Override
	public ExecutionStates close() {
		log.info("Closing CSVSaver... Nothing to do");
		status = ExecutionStates.CLOSED;
		return status;
	}

	@Override
	public boolean save(CollectableData dt, String where) {
		try {
			if (ExecutionStates.READY.equals(status)) {
				String path = FileUtils.CSV_OUTPUT_FILE_PATH + where.replace("Result", "LOG") + FileUtils.CSV;
				File file = new File(path);

				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fop = new FileOutputStream(file, true);
				fop.write(dt.getData().getBytes());
				fop.write(System.getProperty("line.separator").getBytes());
				fop.flush();
				fop.close();
				return true;
			} else {
				log.warn("Not Ready to write file");
			}
		} catch (IOException e1) {
			log.error(e1.getMessage());
		}
		return false;

	}
}
