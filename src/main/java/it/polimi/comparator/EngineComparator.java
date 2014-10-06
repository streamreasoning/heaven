package it.polimi.comparator;

import it.polimi.streamer.EventProcessor;
import it.polimi.teststand.events.Experiment;
import it.polimi.teststand.events.ExperimentResult;
import it.polimi.teststand.events.StreamingEvent;

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
	protected ExperimentResult er;

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

	// TODO can be used to execute a query in the last part
	public abstract void turnOn();

	public abstract void turnOff();

}
