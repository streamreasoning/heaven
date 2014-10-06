package it.polimi.comparator.engine;

import it.polimi.comparator.events.ComparisonExperimentResult;
import it.polimi.comparator.events.ComparisonResultEvent;
import it.polimi.output.result.ResultCollector;
import it.polimi.streamer.EventProcessor;
import it.polimi.teststand.events.Experiment;
import it.polimi.teststand.events.StreamingEvent;
import it.polimi.teststand.events.TestExperimentResultEvent;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public abstract class EngineComparator extends EventProcessor<StreamingEvent> {

	protected static ConsoleAppender appender;
	protected static Logger _logger;
	protected static PatternLayout sl = new PatternLayout(
			"%C{1}-%d{HH:mm:ss.SS} - %t-%x-%-5p-%-10c:%m%n");

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

	protected static void initLogger() {

		appender = new ConsoleAppender(sl);
		_logger = Logger.getRootLogger();
		_logger.addAppender(appender);
		_logger.setLevel((Level) Level.INFO);
	}

	public abstract void turnOn();

	public abstract void turnOff();

	public ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> getResultCollector() {
		return resultCollector;
	}

	public void setResultCollector(ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> resultCollector) {
		System.out.println("set");
		this.resultCollector = resultCollector;
	}

}
