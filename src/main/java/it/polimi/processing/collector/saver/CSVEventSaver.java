package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import lombok.extern.log4j.Log4j;

import org.apache.log4j.Logger;

@Log4j
public class CSVEventSaver implements EventSaver {

	private ExecutionStates status;
	Date d = new Date();
	private final String outputPath;

	public CSVEventSaver(String path) {
		this.outputPath = path;
	}

	public CSVEventSaver() {
		this.outputPath = FileUtils.CSV_OUTPUT_FILE_PATH;
	}

	@Override
	public ExecutionStates init() {
		Logger.getRootLogger().info("Initialising CSVSaver... Nothing to do");
		return status = ExecutionStates.READY;
	}

	@Override
	public ExecutionStates close() {
		Logger.getRootLogger().info("Closing CSVSaver... Nothing to do");
		return status = ExecutionStates.CLOSED;
	}

	@Override
	public boolean save(CollectableData dt, String where) {
		try {
			if (ExecutionStates.READY.equals(status)) {
				String path = outputPath + where.replace("Result", "LOG") + FileUtils.CSV;
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
				Logger.getRootLogger().warn("Not Ready to write file");
				return false;
			}
		} catch (IOException e1) {
			log.error(e1.getMessage());
			return false;
		}

	}
}
