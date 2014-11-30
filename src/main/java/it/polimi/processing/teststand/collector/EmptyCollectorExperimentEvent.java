package it.polimi.processing.teststand.collector;

import it.polimi.processing.Startable;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.teststand.core.TestStand;

import java.io.IOException;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyCollectorExperimentEvent implements StartableCollector<EventResult>, Startable<ExecutionState> {

	private long timestamp;

	private ExecutionState status;

	private TestStand<RSPEngine<RSPEvent>> stand;

	public EmptyCollectorExperimentEvent(TestStand<RSPEngine<RSPEvent>> stand) throws SQLException, ClassNotFoundException {
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

}