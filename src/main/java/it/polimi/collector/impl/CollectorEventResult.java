package it.polimi.collector.impl;

import it.polimi.Startable;
import it.polimi.collector.StartableCollector;
import it.polimi.collector.saver.EventSaver;
import it.polimi.enums.ExecutionStates;
import it.polimi.events.Event;
import it.polimi.events.result.StreamingEventResult;
import it.polimi.rspengine.RSPEngine;
import it.polimi.teststand.core.TestStand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectorEventResult implements
		StartableCollector<StreamingEventResult>, Startable<ExecutionStates> {

	private long timestamp;
	private EventSaver trigSaver;
	private EventSaver csvSaver;

	private ExecutionStates status;

	private TestStand<RSPEngine> stand;

	public CollectorEventResult(TestStand<RSPEngine> stand, EventSaver trig,
			EventSaver csv) throws SQLException, ClassNotFoundException {
		this.stand = stand;
		this.trigSaver = trig;
		this.csvSaver = csv;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionStates.READY;
	}

	@Override
	public boolean store(StreamingEventResult r) throws IOException {
		if (!ExecutionStates.READY.equals(status)) {
			return false;
		} else {
			return trigSaver.save(r) && csvSaver.save(r);
		}
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public ExecutionStates init() {
		try {
			if (ExecutionStates.READY.equals(trigSaver.init())
					&& ExecutionStates.READY.equals(csvSaver.init())) {
				return status = ExecutionStates.READY;
			} else {
				return status = ExecutionStates.ERROR;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return status = ExecutionStates.ERROR;

		}
	}

	@Override
	public ExecutionStates close() {
		try {
			if (ExecutionStates.CLOSED.equals(trigSaver.close())
					&& ExecutionStates.CLOSED.equals(csvSaver.close())) {
				return status = ExecutionStates.CLOSED;
			} else {
				return status = ExecutionStates.ERROR;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return status = ExecutionStates.ERROR;

		}
	}

	@Override
	public StreamingEventResult newEventInstance(Set<String[]> all_triples,
			Event e) {
		return stand.newEventInstance(all_triples, e);
	}

}
