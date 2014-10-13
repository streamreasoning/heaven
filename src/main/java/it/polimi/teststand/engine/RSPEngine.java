package it.polimi.teststand.engine;

import it.polimi.EventProcessor;
import it.polimi.collector.ResultCollector;
import it.polimi.enums.ExecutionStates;
import it.polimi.events.StreamingEvent;
import it.polimi.events.result.StreamingEventResult;
import it.polimi.teststand.core.Stand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RSPEngine implements EventProcessor<StreamingEvent> {

	protected ExecutionStates status;
	protected ResultCollector<StreamingEventResult> collector;
	protected String name;
	protected Stand stand;

	public RSPEngine(ResultCollector<StreamingEventResult> stand) {
		this.collector = stand;
	}

	public abstract ExecutionStates init();

	public abstract ExecutionStates close();

	public abstract ExecutionStates startProcessing();

	public abstract ExecutionStates stopProcessing();

}
