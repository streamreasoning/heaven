package it.polimi.collector.saver;

import it.polimi.collector.Collectable;
import it.polimi.enums.ExecutionStates;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class TrigEventSaver implements EventSaver {

	public static final String OUTPUT_FILE_PATH = "src/main/resource/data/output/trig/";
	public static final String INPUT_FILE_PATH = "src/main/resource/data/input/";
	public static final String FILE_EXTENSION = "trig";

	private ExecutionStates status;

	@Override
	public boolean save(Collectable e) {
		try {
			if (ExecutionStates.READY.equals(status)) {
				String path = OUTPUT_FILE_PATH + e.getName() + FILE_EXTENSION;
				Logger.getRootLogger().debug("TRIG FILE PATH " + path);
				File file = new File(path);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fop = new FileOutputStream(file, true);
				fop.write(e.getBytes());
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
