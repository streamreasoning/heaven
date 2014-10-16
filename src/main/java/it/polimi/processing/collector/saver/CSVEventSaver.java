package it.polimi.processing.collector.saver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import it.polimi.processing.collector.Collectable;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.utils.FileUtils;

public class CSVEventSaver implements EventSaver {

	private ExecutionStates status;

	@Override
	public boolean save(Collectable e) {
		try {
			if (ExecutionStates.READY.equals(status)) {
				String path = FileUtils.CSV_OUTPUT_FILE_PATH + e.getName()+FileUtils.CSV;
				File file = new File(path);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fop = new FileOutputStream(file, true);
				fop.write(e.getCSV().getBytes());
				fop.write(System.getProperty("line.separator").getBytes());
				fop.flush();
				fop.close();
				return true;
			} else {
				Logger.getRootLogger().warn("Not Ready to write file");
				return false;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

	}

	@Override
	public ExecutionStates init() throws ClassNotFoundException, SQLException {
		Logger.getRootLogger().info("Nothing to do");
		return status = ExecutionStates.READY;
	}

	@Override
	public ExecutionStates close() throws ClassNotFoundException, SQLException {
		Logger.getRootLogger().info("Nothing to do");
		return status = ExecutionStates.CLOSED;
	}

}
