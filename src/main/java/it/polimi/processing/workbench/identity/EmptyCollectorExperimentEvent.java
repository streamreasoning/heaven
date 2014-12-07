package it.polimi.processing.workbench.identity;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.workbench.core.RSPTestStand;
import it.polimi.processing.workbench.core.Startable;

import java.io.IOException;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyCollectorExperimentEvent implements StartableCollector<EventResult>, Startable<ExecutionState> {

	private long timestamp;

	private ExecutionState status;

	private RSPTestStand stand;

	public EmptyCollectorExperimentEvent(RSPTestStand stand) throws SQLException, ClassNotFoundException {
		this.stand = stand;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionState.READY;
	}

	@Override
	public boolean process(EventResult r) {
		return true;
	}

	@Override
	public ExecutionState init() {
		status = ExecutionState.READY;
		return status;
	}

	@Override
	public ExecutionState close() {
		status = ExecutionState.CLOSED;
		return status;
	}

	@Override
	public boolean process(EventResult r, String where) throws IOException {
		return true;
	}

	@Override
	public boolean processDone() {
		// TODO Auto-generated method stub
		return false;
	}

}
