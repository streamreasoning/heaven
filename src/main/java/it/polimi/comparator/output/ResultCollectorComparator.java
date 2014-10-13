package it.polimi.comparator.output;

import it.polimi.collector.ResultCollector;
import it.polimi.comparator.events.ComparisonExperimentResult;
import it.polimi.comparator.events.ComparisonResultEvent;
import it.polimi.enums.ExecutionStates;
import it.polimi.output.filesystem.DatabaseManager;
import it.polimi.output.filesystem.FileManager;
import it.polimi.output.sqllite.DatabaseManagerImpl;

import java.io.IOException;
import java.sql.SQLException;

import lombok.Getter;

@Getter
public class ResultCollectorComparator implements
		ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> {

	private long timestamp;
	private FileManager fs;
	private DatabaseManager db;
	private ExecutionStates status;

	public ResultCollectorComparator(FileManager fs, DatabaseManager db)
			throws SQLException, ClassNotFoundException {
		this.fs = fs;
		this.db = db;
		timestamp = System.currentTimeMillis();
	}

	@Override
	public boolean storeExperimentResult(ComparisonExperimentResult r) {
		// TODO experiment
		if (!ExecutionStates.READY.equals(status)) {
			return false;
		} else {
			String q = DatabaseManagerImpl.COMPARATION_INSERT + r.toString();
			return db.executeSql(q);
		}

	}

	@Override
	public boolean storeEventResult(ComparisonResultEvent r) throws IOException {
		if (!ExecutionStates.READY.equals(status)) {
			return false;
		} else {
			String q = DatabaseManagerImpl.COMPARATION_INSERT + r.toString();
			return db.executeSql(q);
		}

	}

	public boolean start() {
		try {
			fs.init();
			db.init();
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;

		}
	}

	public long stop() {
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
	public long getTimestamp() {
		return timestamp;
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
