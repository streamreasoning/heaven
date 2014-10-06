package it.polimi.output.result;

import it.polimi.output.filesystem.FileManager;
import it.polimi.output.sqllite.DatabaseManager;
import it.polimi.teststand.events.EventResult;
import it.polimi.teststand.events.ExperimentResult;

import java.io.IOException;
import java.sql.SQLException;

public class ResultCollectorImpl implements ResultCollector {

	private long timestamp;
	private FileManager fs;
	private DatabaseManager db;

	public ResultCollectorImpl(FileManager fs, DatabaseManager db)
			throws SQLException, ClassNotFoundException {
		this.fs = fs;
		this.db = db;
		timestamp = System.currentTimeMillis();
	}

	@Override
	public boolean storeExperimentResult(ExperimentResult r) {
		String q = "INSERT INTO EXPERIMENT (EXP_ID, TS_INIT, TS_END, INPUT_FILE,RESULT_FILE, FILE_LOG_FOLDER) "
				+ r.toString();

		return db.executeSql(q);

	}

	@Override
	public boolean storeEventResult(EventResult r) throws IOException {
		fs.toFile(r.getOutputFileName(), r); // TrigFile
		long queryLatency = r.getResultTimestamp() - r.getEvent_timestamp();
		fs.toFile(FileManager.LOG_PATH + r.getFolder(),
				"LOG_" + r.getExperiment_timestamp(), (r.getStartTripleEvent()
						+ "," + r.getEvent_id() + ",Memory," + queryLatency)
						.getBytes());
		return true;
	}

	@Override
	public long start() {
		try {
			fs.init();
			db.init();
			return System.currentTimeMillis();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return System.currentTimeMillis();

		}
	}

	@Override
	public long stop() {
		try {
			fs.close();
			db.close();
			return System.currentTimeMillis();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return System.currentTimeMillis();

		}
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}
}
