package it.polimi.processing.workbench.collector;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.workbench.core.EventProcessor;
import it.polimi.processing.workbench.core.Startable;
import it.polimi.utils.FileUtils;

import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectorEventResult implements StartableCollector<EventResult>, Startable<ExecutionState> {

	private ExecutionState status;
	private String where;

	private EventProcessor<Event> stand;
	private EventResult currentResult;

	public CollectorEventResult(EventProcessor<Event> processor, String where) throws SQLException, ClassNotFoundException {
		this.stand = processor;
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
		return currentResult.getTrig().save(getTrigPath(this.where)) && currentResult.getCSV().save(getCSVpath(this.where));
	}

	@Override
	public boolean process(EventResult r, String w) {
		return !ExecutionState.READY.equals(status) ? false : r.getTrig().save(getTrigPath(w)) && r.getCSV().save(getCSVpath(w));
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
