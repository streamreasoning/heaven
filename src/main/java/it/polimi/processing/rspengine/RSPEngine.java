package it.polimi.processing.rspengine;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.EventResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RSPEngine implements EventProcessor<RSPEvent> {

	protected ExecutionState status;
	protected ResultCollector<EventResult> collector;
	protected String name;

	@Getter
	protected RSPEvent currentEvent = null;

	public RSPEngine(String name, ResultCollector<EventResult> stand) {
		this.collector = stand;
		this.name = name;
	}

	public abstract ExecutionState init();

	public abstract ExecutionState close();

	public abstract ExecutionState startProcessing();

	public abstract ExecutionState stopProcessing();

	public abstract int getEventNumber();

}
