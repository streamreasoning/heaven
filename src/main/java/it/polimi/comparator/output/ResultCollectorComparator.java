package it.polimi.comparator.output;

import it.polimi.comparator.events.ComparisonExperimentResult;
import it.polimi.comparator.events.ComparisonResultEvent;
import it.polimi.output.filesystem.FileManager;
import it.polimi.output.result.ResultCollector;
import it.polimi.output.sqllite.DatabaseManager;

import java.io.IOException;
import java.sql.SQLException;

public class ResultCollectorComparator implements
		ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> {

	private long timestamp;
	private FileManager fs;
	private DatabaseManager db;

	public ResultCollectorComparator(FileManager fs, DatabaseManager db)
			throws SQLException, ClassNotFoundException {
		this.fs = fs;
		this.db = db;
		timestamp = System.currentTimeMillis();
	}

	@Override
	public boolean storeExperimentResult(ComparisonExperimentResult r) {
		//TODO experiment
		String q = DatabaseManager.COMPARATION_INSERT + r.toString();
		return db.executeSql(q);

	}

	@Override
	public boolean storeEventResult(ComparisonResultEvent r) throws IOException {
		String q = DatabaseManager.COMPARATION_INSERT + r.toString();
		return db.executeSql(q);

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
