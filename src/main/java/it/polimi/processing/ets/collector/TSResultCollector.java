package it.polimi.processing.ets.collector;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.results.EventResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TSResultCollector implements ResultCollector {

	private ExecutionState status;
	private String where;

	private EventResult currentResult;

	public TSResultCollector(String where) {
		this.status = ExecutionState.READY;
		this.where = where;
	}

	@Override
	public boolean process(EventResult r) {
		this.currentResult = r;
		return !ExecutionState.READY.equals(status) ? false : currentResult.save(this.where);
	}

	@Override
	public boolean process(EventResult r, String w) {
		currentResult = r;
		return !ExecutionState.READY.equals(status) ? false : currentResult.save(where);
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
