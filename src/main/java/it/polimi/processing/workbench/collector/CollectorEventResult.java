package it.polimi.processing.workbench.collector;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.EventSaver;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.workbench.core.RSPTestStand;
import it.polimi.processing.workbench.core.Startable;

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

	private RSPTestStand stand;

	public CollectorEventResult(RSPTestStand stand, EventSaver trig, EventSaver csv, String where) throws SQLException, ClassNotFoundException {
		this.stand = stand;
		this.trigSaver = trig;
		this.csvSaver = csv;
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionState.READY;
		this.where = where;
	}

	@Override
	public boolean process(EventResult r) throws IOException {
		if (!ExecutionState.READY.equals(status)) {
			return false;
		} else {
			return trigSaver.save(r.getTrig(), this.where) && csvSaver.save(r.getCSV(), this.where);
		}
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
