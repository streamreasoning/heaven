package it.polimi.processing.collector.saver;

import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.enums.ExecutionState;
import lombok.extern.log4j.Log4j;

@Log4j
public class VoidSaver implements EventSaver {

	@Override
	public boolean save(CollectableData d, String where) {
		log.info("Save Protected");
		return true;
	}

	@Override
	public ExecutionState init() {
		return ExecutionState.READY;
	}

	@Override
	public ExecutionState close() {
		return ExecutionState.CLOSED;
	}
}
