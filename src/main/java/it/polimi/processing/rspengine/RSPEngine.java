package it.polimi.processing.rspengine;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.core.Stand;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.events.result.StreamingEventResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RSPEngine implements EventProcessor<StreamingEvent> {

	protected ExecutionStates status;
	protected ResultCollector<StreamingEventResult> collector;
	protected String name;
	protected Stand stand;

	public RSPEngine(String name, ResultCollector<StreamingEventResult> stand) {
		this.collector = stand;
		this.name = name;
	}

	public abstract ExecutionStates init();

	public abstract ExecutionStates close();

	public abstract ExecutionStates startProcessing();

	public abstract ExecutionStates stopProcessing();

}
