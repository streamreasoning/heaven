package it.polimi.processing.ets.collector;

import it.polimi.processing.Startable;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.results.EventResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectorEventResult implements StartableCollector<EventResult>, Startable<ExecutionState> {

	private ExecutionState status;
	private String where;

	private EventResult currentResult;

	public CollectorEventResult(String where) {
		this.status = ExecutionState.READY;
		this.where = where;
	}

	@Override
	public boolean process(EventResult r) {
		this.currentResult = r;
		return !ExecutionState.READY.equals(status) ? false : processDone();
	}

	@Override
	public boolean processDone() {
		return !ExecutionState.READY.equals(status) ? false : currentResult.save(where);
	}

	@Override
	public boolean process(EventResult r, String w) {
		currentResult = r;
		this.where = w;
		return processDone();
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
