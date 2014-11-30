package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import lombok.extern.log4j.Log4j;

@Log4j
public class CSVEventSaver implements EventSaver {

	private ExecutionState status;
	Date d = new Date();
	private final String outputPath;
	private String path = "";
	private File file;

	private FileWriter writer;

	public CSVEventSaver(String path) {
		this.outputPath = path;
	}

	public CSVEventSaver() {
		this.outputPath = FileUtils.CSV_OUTPUT_FILE_PATH;
	}

	@Override
	public ExecutionState init() {
		log.info("Initialising CSVSaver... Nothing to do");
		status = ExecutionState.READY;
		return status;
	}

	@Override
	public ExecutionState close() {
		log.info("Closing CSVSaver... Nothing to do");
		status = ExecutionState.CLOSED;
		try {
			writer.close();
		} catch (IOException e) {
			log.error(e.getMessage());
			status = ExecutionState.ERROR;
		}
		return status;

	}

	@Override
	public boolean save(CollectableData dt, String where) {
		try {
			if (ExecutionState.READY.equals(status)) {

				path = outputPath + where.replace("Result", "LOG") + FileUtils.CSV;
				file = new File(path);
				writer = new FileWriter(file, true);

				if (!file.exists()) {
					file.createNewFile();
				}
				writer.write(dt.getData());
				writer.write(System.getProperty("line.separator"));
				writer.flush();

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
