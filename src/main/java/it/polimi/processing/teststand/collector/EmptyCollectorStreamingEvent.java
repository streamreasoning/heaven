package it.polimi.processing.teststand.collector;

import it.polimi.processing.Startable;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.result.StreamingEventResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.teststand.core.TestStand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyCollectorStreamingEvent implements StartableCollector<StreamingEventResult>, Startable<ExecutionStates> {

	private long timestamp;

	private ExecutionStates status;

	private TestStand<RSPEngine> stand;

	public EmptyCollectorStreamingEvent(TestStand<RSPEngine> stand) throws SQLException, ClassNotFoundException {
		this.stand = stand;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionStates.READY;
	}

	@Override
	public boolean store(StreamingEventResult r, String where) throws IOException {
		return true;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
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
	public StreamingEventResult newEventInstance(Set<String[]> allTriples, Event e) {
		return stand.newEventInstance(allTriples, e);
	}

}
