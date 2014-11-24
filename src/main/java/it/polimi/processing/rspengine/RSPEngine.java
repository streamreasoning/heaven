package it.polimi.processing.rspengine;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.interfaces.EventResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RSPEngine<TestStandEvent> implements EventProcessor<TestStandEvent> {

	protected ExecutionStates status;
	protected ResultCollector<EventResult> collector;
	protected String name;

	public RSPEngine(String name, ResultCollector<EventResult> stand) {
		this.collector = stand;
		this.name = name;
	}

	public abstract ExecutionStates init();

	public abstract ExecutionStates close();

	public abstract ExecutionStates startProcessing();

	public abstract ExecutionStates stopProcessing();

}
