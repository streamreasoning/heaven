package it.polimi.processing.teststand.collector;

import it.polimi.processing.Startable;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.EventSaver;
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
public class CollectorEventResult implements StartableCollector<EventResult>, Startable<ExecutionStates> {

	private long timestamp;
	private EventSaver trigSaver;
	private EventSaver csvSaver;

	private ExecutionStates status;
	private String where;

	private TestStand<RSPEngine<TestStandEvent>> stand;

	public CollectorEventResult(TestStand<RSPEngine<TestStandEvent>> stand, EventSaver trig, EventSaver csv, String where) throws SQLException,
			ClassNotFoundException {
		this.stand = stand;
		this.trigSaver = trig;
		this.csvSaver = csv;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionStates.READY;
	}

	@Override
	public boolean store(EventResult r) throws IOException {
		if (!ExecutionStates.READY.equals(status)) {
			return false;
		} else {
			return trigSaver.save(r.getTrig(), where) && csvSaver.save(r.getCSV(), this.where);
		}
	}

	@Override
	public ExecutionStates init() {
		if (ExecutionStates.READY.equals(trigSaver.init()) && ExecutionStates.READY.equals(csvSaver.init())) {
			status = ExecutionStates.READY;
		} else {
			status = ExecutionStates.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionStates close() {
		if (ExecutionStates.CLOSED.equals(trigSaver.close()) && ExecutionStates.CLOSED.equals(csvSaver.close())) {
			status = ExecutionStates.CLOSED;
		} else {
			status = ExecutionStates.ERROR;
		}
		return status;
	}

	@Override
	public EventResult newEventInstance(Set<String[]> allTriples, Event e) {
		return stand.newEventInstance(allTriples, e);
	}

}
