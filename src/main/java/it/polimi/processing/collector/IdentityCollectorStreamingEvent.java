package it.polimi.processing.collector;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.ets.core.Startable;
import it.polimi.processing.events.results.EventResult;

import java.sql.SQLException;

import lombok.extern.log4j.Log4j;

@Log4j
/**
 * Identity Class, Useful for testing
 */
public final class IdentityCollectorStreamingEvent implements StartableCollector<EventResult>, Startable<ExecutionState> {

	private ExecutionState status;

	public IdentityCollectorStreamingEvent() throws SQLException, ClassNotFoundException {
		this.status = ExecutionState.READY;
	}

	@Override
	public boolean process(EventResult r) {
		return processDone();
	}

	@Override
	public boolean process(EventResult r, String where) {
		return processDone();
	}

	@Override
	public boolean processDone() {
		log.info("Nothing To Do, this is the Identity Implementation");
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
