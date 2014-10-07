package it.polimi.comparator.engine;

import it.polimi.comparator.events.ComparisonExperimentResult;
import it.polimi.comparator.events.ComparisonResultEvent;
import it.polimi.output.result.ResultCollector;
import it.polimi.streamer.EventProcessor;
import it.polimi.teststand.events.Experiment;
import it.polimi.teststand.events.StreamingEvent;
import it.polimi.teststand.events.TestExperimentResultEvent;

public abstract class EngineComparator extends EventProcessor<StreamingEvent> {


	protected Experiment experiment;
	protected TestExperimentResultEvent er;
	protected ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> resultCollector;

	public EngineComparator(
			ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> resultCollector,
			String name) {
		this.resultCollector = resultCollector;
		this.name = name;
	}

	protected String name;

	protected static int time = 0;

	public String getName() {
		return name;
	}

	public abstract boolean startProcessing(Experiment e);

	public abstract Experiment stopProcessing();


	public abstract void turnOn();

	public abstract void turnOff();

	public ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> getResultCollector() {
		return resultCollector;
	}

	public void setResultCollector(ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> resultCollector) {
		this.resultCollector = resultCollector;
	}

}
