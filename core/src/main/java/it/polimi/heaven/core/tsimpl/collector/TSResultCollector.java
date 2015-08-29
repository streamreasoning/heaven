package it.polimi.heaven.core.tsimpl.collector;

import it.polimi.heaven.core.enums.ExecutionState;
import it.polimi.heaven.core.ts.collector.ResultCollector;
import it.polimi.heaven.core.ts.events.EventResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class TSResultCollector implements ResultCollector {

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
		return !ExecutionState.READY.equals(status) ? false : currentResult
				.save(w);
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
