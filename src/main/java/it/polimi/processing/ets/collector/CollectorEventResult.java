package it.polimi.processing.ets.collector;

import it.polimi.processing.Startable;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.results.EventResult;
import it.polimi.utils.FileUtils;
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
		return !ExecutionState.READY.equals(status) ? false : currentResult.saveTrig(getTrigPath(where)) && currentResult.saveCSV(getCSVpath(where));
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

	private String getTrigPath(String w) {
		return FileUtils.TRIG_OUTPUT_FILE_PATH + w + FileUtils.TRIG_FILE_EXTENSION;
	}

	private String getCSVpath(String w) {
		String csvLog = w.replace("0Result", "RESLOG").replace("0Window", "WINLOG").replace("1Result", "LATLOG").replace("1Window", "WINLATLOG")
				.replace("2Result", "MEMLOG").replace("2Window", "WINMEMLOG");
		return FileUtils.CSV_OUTPUT_FILE_PATH + csvLog + FileUtils.CSV;
	}
}
