package it.polimi.teststand.output;

import it.polimi.enums.ExecutionStates;
import it.polimi.output.filesystem.FileManager;
import it.polimi.output.result.ResultCollector;
import it.polimi.output.sqllite.DatabaseManager;
import it.polimi.output.sqllite.DatabaseManagerImpl;
import it.polimi.teststand.events.TestExperimentResultEvent;
import it.polimi.teststand.events.TestResultEvent;

import java.io.IOException;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultCollectorTestStandImpl implements
		ResultCollector<TestResultEvent, TestExperimentResultEvent> {

	private long timestamp;
	private FileManager fs;
	private DatabaseManager db;
	private ExecutionStates status;

	public ResultCollectorTestStandImpl(FileManager fs, DatabaseManager db)
			throws SQLException, ClassNotFoundException {
		this.fs = fs;
		this.db = db;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionStates.READY;
	}

	@Override
	public boolean storeExperimentResult(TestExperimentResultEvent r) {
		if (!ExecutionStates.READY.equals(status)) {
			return false;
		} else {
			String q = DatabaseManagerImpl.EXPERIMENT_INSERT + r.toString();
			return db.executeSql(q);
		}
	}

	@Override
	public boolean storeEventResult(TestResultEvent r) throws IOException {
		if (!ExecutionStates.READY.equals(status)) {
			return false;
		} else {
			fs.toFile(r.getOutputFileName(), r); // TrigFile
			long queryLatency = r.getResult_timestamp()
					- r.getEvent_timestamp();
			fs.toFile(
					FileManager.LOG_PATH + r.getFolder(),
					"LOG_" + r.getExperiment_timestamp() + "_"
							+ r.getExperiment_id(),
					(r.getEvent_id() + "," + r.getEvent_timestamp()
							+ ",Memory," + queryLatency).getBytes());
			return true;
		}
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	private boolean start() {
		try {
			fs.init();
			db.init();
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;

		}
	}

	private long stop() {
		try {
			fs.close();
			db.close();
			return System.currentTimeMillis();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return 0;

		}
	}

	@Override
	public ExecutionStates init() {
		if (start()) {
			return status = ExecutionStates.READY;
		}
		return status = ExecutionStates.ERROR;
	}

	@Override
	public ExecutionStates close() {
		if (stop() != 0) {
			return status = ExecutionStates.CLOSED;
		}
		return status = ExecutionStates.ERROR;
	}
}
