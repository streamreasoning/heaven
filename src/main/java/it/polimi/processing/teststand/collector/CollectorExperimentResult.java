package it.polimi.processing.teststand.collector;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.EventSaver;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.teststand.core.RSPWorkBench;

import java.io.IOException;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectorExperimentResult implements StartableCollector<ExperimentResult> {

	private long timestamp;
	private EventSaver sqlLiteSaver;
	private RSPWorkBench stand;
	private ExecutionState status;
	private String where;

	public CollectorExperimentResult(RSPWorkBench stand, EventSaver saver) throws SQLException, ClassNotFoundException {
		this.stand = stand;
		this.sqlLiteSaver = saver;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionState.READY;
	}

	@Override
	public boolean store(ExperimentResult r) throws IOException {
		if (!ExecutionState.READY.equals(status)) {
			return false;
		} else {
			return sqlLiteSaver.save(r.getSQL(), this.where);

		}
	}

	@Override
	public boolean store(ExperimentResult r, String where) throws IOException {

		if (!ExecutionState.READY.equals(status)) {
			return false;
		} else {
			return sqlLiteSaver.save(r.getSQL(), where);

		}
	}

	@Override
	public ExecutionState init() {
		if (ExecutionState.READY.equals(sqlLiteSaver.init())) {
			status = ExecutionState.READY;
		} else {
			status = ExecutionState.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionState close() {
		if (ExecutionState.CLOSED.equals(sqlLiteSaver.close())) {
			status = ExecutionState.CLOSED;
		} else {
			status = ExecutionState.ERROR;
		}
		return status;
	}

}
