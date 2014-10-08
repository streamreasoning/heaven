package it.polimi.teststand.engine;

import it.polimi.enums.ExecutionStates;
import it.polimi.events.Experiment;
import it.polimi.events.StreamingEvent;
import it.polimi.output.result.ResultCollector;
import it.polimi.streamer.EventProcessor;
import it.polimi.teststand.events.TestExperimentResultEvent;
import it.polimi.teststand.events.TestResultEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RSPEngine extends EventProcessor<StreamingEvent> {

	protected ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector;
	protected TestExperimentResultEvent er;
	protected Experiment experiment;
	protected String name;
	protected ExecutionStates status;

	public RSPEngine(
			ResultCollector<TestResultEvent, TestExperimentResultEvent> storeSystem) {
		this.resultCollector = storeSystem;
	}

	public abstract ExecutionStates init();

	public abstract ExecutionStates close();

	public abstract ExecutionStates startProcessing(Experiment e);

	public abstract ExecutionStates stopProcessing(Experiment e);

}
