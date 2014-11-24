package it.polimi.processing.teststand.collector;

import it.polimi.processing.Startable;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.TestStandEvent;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.teststand.core.TestStand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyCollectorExperimentEvent implements StartableCollector<EventResult>, Startable<ExecutionStates> {

	private long timestamp;

	private ExecutionStates status;

	private TestStand<RSPEngine<TestStandEvent>> stand;

	public EmptyCollectorExperimentEvent(TestStand<RSPEngine<TestStandEvent>> stand) throws SQLException, ClassNotFoundException {
		this.stand = stand;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionStates.READY;
	}

	@Override
	public boolean store(EventResult r) throws IOException {
		return true;
	}

	@Override
	public ExecutionStates init() {
		status = ExecutionStates.READY;
		return status;
	}

	@Override
	public ExecutionStates close() {
		status = ExecutionStates.CLOSED;
		return status;
	}

	@Override
	public EventResult newEventInstance(Set<String[]> allTriples, Event e) {
		throw new RuntimeException("Not Implemented, Remove");
	}

}
