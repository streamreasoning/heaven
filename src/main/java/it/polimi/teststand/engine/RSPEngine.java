package it.polimi.teststand.engine;

import it.polimi.output.result.ResultCollector;
import it.polimi.streamer.EventProcessor;
import it.polimi.teststand.events.Experiment;
import it.polimi.teststand.events.TestExperimentResultEvent;
import it.polimi.teststand.events.StreamingEvent;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.time.CurrentTimeEvent;

public abstract class RSPEngine extends EventProcessor<StreamingEvent> {

	protected static Configuration cepConfig;
	protected static EPServiceProvider cep;
	protected static EPRuntime cepRT;
	protected static EPAdministrator cepAdm;
	protected static ConfigurationMethodRef ref;

	protected static ConsoleAppender appender;
	protected static Logger _logger;
	protected static PatternLayout sl = new PatternLayout(
			"%C{1}-%d{HH:mm:ss.SS} - %t-%x-%-5p-%-10c:%m%n");
	protected ResultCollector resultCollector;

	protected Experiment experiment;
	protected TestExperimentResultEvent er;

	protected String name;

	protected static int time = 0;
	protected static Boolean BUSY = false;

	public RSPEngine(ResultCollector storeSystem) {
		this.setResultCollector(storeSystem);
	}

	public String getName() {
		return name;
	}

	public abstract boolean startProcessing(Experiment e);

	public abstract Experiment stopProcessing();

	public boolean isBusy() {
		return BUSY;
	}

	protected void sendTimeEvent() {
		time += 1000;
		cepRT.sendEvent(new CurrentTimeEvent(time));
	}

	protected static void initLogger() {

		appender = new ConsoleAppender(sl);
		_logger = Logger.getRootLogger();
		_logger.addAppender(appender);
		_logger.setLevel((Level) Level.INFO);
	}

	// TODO can be used to execute a query in the last part
	public abstract void turnOn();

	public abstract void turnOff();

	public ResultCollector getResultCollector() {
		return resultCollector;
	}

	public void setResultCollector(ResultCollector resultCollector) {
		this.resultCollector = resultCollector;
	}

}
