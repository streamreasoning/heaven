package it.polimi.processing.teststand.collector;

import it.polimi.processing.Startable;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.teststand.core.RSPWorkBench;

import java.io.IOException;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyCollectorStreamingEvent implements StartableCollector<EventResult>, Startable<ExecutionState> {

	private long timestamp;

	private ExecutionState status;

	private RSPWorkBench stand;

	public EmptyCollectorStreamingEvent(RSPWorkBench stand) throws SQLException, ClassNotFoundException {
		this.stand = stand;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionState.READY;
	}

	@Override
	public boolean store(EventResult r) throws IOException {
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
	public boolean store(EventResult r, String where) throws IOException {
		return true;
	}

}
