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
	private String outputPath;
	private EventResult currentResult;

	public TSResultCollector(String outputPath) {
		this.status = ExecutionState.READY;
		this.outputPath = outputPath;
	}

	@Override
	public boolean process(EventResult r) {
		this.currentResult = r;
		return !ExecutionState.READY.equals(status) ? false : currentResult.save(this.outputPath);
	}

	@Override
	public boolean process(EventResult r, String w) {
		currentResult = r;
		return !ExecutionState.READY.equals(status) ? false : currentResult.save(w);
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
