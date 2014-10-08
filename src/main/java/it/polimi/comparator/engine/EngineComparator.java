package it.polimi.comparator.engine;

import it.polimi.comparator.events.ComparisonExperimentResult;
import it.polimi.comparator.events.ComparisonResultEvent;
import it.polimi.enums.ExecutionStates;
import it.polimi.events.Experiment;
import it.polimi.events.StreamingEvent;
import it.polimi.output.result.ResultCollector;
import it.polimi.streamer.EventProcessor;
import it.polimi.teststand.events.TestExperimentResultEvent;
import lombok.Getter;

@Getter
public abstract class EngineComparator extends EventProcessor<StreamingEvent> {

	protected Experiment experiment;
	protected TestExperimentResultEvent er;
	protected ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> resultCollector;
	protected ExecutionStates status;

	public EngineComparator(
			ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> resultCollector,
			String name) {
		this.resultCollector = resultCollector;
		this.name = name;
	}

	public abstract ExecutionStates startProcessing(Experiment e);

	public abstract ExecutionStates stopProcessing(Experiment e);

	public abstract ExecutionStates init();

	public abstract ExecutionStates close();

}
