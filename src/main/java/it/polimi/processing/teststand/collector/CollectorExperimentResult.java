package it.polimi.processing.teststand.collector;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.EventSaver;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.TestStandEvent;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.teststand.core.TestStand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectorExperimentResult implements StartableCollector<ExperimentResult> {

	private long timestamp;
	private EventSaver sqlLiteSaver;
	private TestStand<RSPEngine<TestStandEvent>> stand;
	private ExecutionStates status;
	private String where;

	public CollectorExperimentResult(TestStand<RSPEngine<TestStandEvent>> stand, EventSaver saver) throws SQLException, ClassNotFoundException {
		this.stand = stand;
		this.sqlLiteSaver = saver;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionStates.READY;
	}

	@Override
	public boolean store(ExperimentResult r) throws IOException {
		if (!ExecutionStates.READY.equals(status)) {
			return false;
		} else {
			return sqlLiteSaver.save(r.getSQL(), this.where);

		}
	}

	@Override
	public ExecutionStates init() {
		if (ExecutionStates.READY.equals(sqlLiteSaver.init())) {
			status = ExecutionStates.READY;
		} else {
			status = ExecutionStates.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionStates close() {
		if (ExecutionStates.CLOSED.equals(sqlLiteSaver.close())) {
			status = ExecutionStates.CLOSED;
		} else {
			status = ExecutionStates.ERROR;
		}
		return status;
	}

	@Override
	public ExperimentResult newEventInstance(Set<String[]> allTriples, Event e) {
		throw new RuntimeException("Not Implemented, Remove");
	}
}
