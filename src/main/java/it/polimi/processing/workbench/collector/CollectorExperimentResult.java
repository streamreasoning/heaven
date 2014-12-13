package it.polimi.processing.workbench.collector;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.ExperimentResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectorExperimentResult implements StartableCollector<ExperimentResult> {

	private long timestamp;
	private ExecutionState status;
	private String where;
	private ExperimentResult currentExperiment;

	public CollectorExperimentResult() {
		this.timestamp = System.currentTimeMillis();
		this.status = ExecutionState.READY;
	}

	@Override
	public boolean process(ExperimentResult r) {
		this.currentExperiment = r;
		return !ExecutionState.READY.equals(status) ? false : processDone();
	}

	@Override
	public boolean processDone() {
		return currentExperiment.getSQL().save(where);
	}

	@Override
	public boolean process(ExperimentResult r, String where) {
		return !ExecutionState.READY.equals(status) ? false : r.getSQL().save(where);
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
