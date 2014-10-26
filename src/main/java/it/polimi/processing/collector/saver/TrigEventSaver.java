package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class TrigEventSaver implements EventSaver {

	private ExecutionStates status;

	@Override
	public boolean save(CollectableData d) {
		try {
			if (ExecutionStates.READY.equals(status)) {
				String path = FileUtils.TRIG_OUTPUT_FILE_PATH + d.getName()
						+ FileUtils.TRIG_FILE_EXTENSION;
				Logger.getRootLogger().debug("TRIG FILE PATH " + path);
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
		Logger.getRootLogger().info("Initialising TrigSaver... Nothing to do");
		return status = ExecutionStates.READY;
	}

	@Override
	public ExecutionStates close() throws ClassNotFoundException, SQLException {
		Logger.getRootLogger().info("Closing TrigSaver... Nothing to do");
		return status = ExecutionStates.CLOSED;
	}

}
