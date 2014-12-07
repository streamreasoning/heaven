package it.polimi.processing.workbench.collector;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.EventSaver;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.workbench.core.Startable;
import it.polimi.processing.workbench.core.TestStand;

import java.io.IOException;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Getter
@Setter
@Log4j
public class CollectorEventResult implements StartableCollector<EventResult>, Startable<ExecutionState> {

	private long timestamp;
	private EventSaver trigSaver;
	private EventSaver csvSaver;

	private ExecutionState status;
	private String where;

	private TestStand<Event> stand;
	private EventResult currentResult;

	public CollectorEventResult(TestStand<Event> stand, EventSaver trig, EventSaver csv, String where) throws SQLException, ClassNotFoundException {
		this.stand = stand;
		this.trigSaver = trig;
		this.csvSaver = csv;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionState.READY;
		this.where = where;
	}

	@Override
	public boolean process(EventResult r) {
		this.currentResult = r;
		if (!ExecutionState.READY.equals(status)) {
			return false;
		} else {
			return processDone();
		}
	}

	@Override
	public boolean processDone() {
		return trigSaver.save(currentResult.getTrig(), this.where) && csvSaver.save(currentResult.getCSV(), this.where);
	}

	@Override
	public boolean process(EventResult r, String w) throws IOException {
		log.debug("Store [" + w + "]");
		if (!ExecutionState.READY.equals(status)) {
			return false;
		} else {
			return trigSaver.save(r.getTrig(), w) && csvSaver.save(r.getCSV(), w);
		}
	}

	@Override
	public ExecutionState init() {
		if (ExecutionState.READY.equals(trigSaver.init()) && ExecutionState.READY.equals(csvSaver.init())) {
			status = ExecutionState.READY;
		} else {
			status = ExecutionState.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionState close() {
		if (ExecutionState.CLOSED.equals(trigSaver.close()) && ExecutionState.CLOSED.equals(csvSaver.close())) {
			status = ExecutionState.CLOSED;
		} else {
			status = ExecutionState.ERROR;
		}
		return status;
	}

}
