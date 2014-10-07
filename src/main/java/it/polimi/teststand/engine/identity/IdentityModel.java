package it.polimi.teststand.engine.identity;

import it.polimi.events.Experiment;
import it.polimi.events.StreamingEvent;
import it.polimi.output.filesystem.FileManagerImpl;
import it.polimi.output.result.ResultCollector;
import it.polimi.teststand.engine.RSPEngine;
import it.polimi.teststand.events.TestResultEvent;
import it.polimi.teststand.events.TestExperimentResultEvent;

import java.io.IOException;

import org.apache.log4j.Logger;

public class IdentityModel extends RSPEngine {

	public IdentityModel(
			ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector) {
		super(resultCollector);
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		if (e.isIgnore()) {
			Logger.getRootLogger().debug("Ignored");
			return false;
		}

		TestResultEvent r = new TestResultEvent(e.getEventTriples(),
				e.getEventTriples(), e.getEvent_timestamp(),
				experiment.getOutputFileName(), "empty/", experiment.getName(),
				experiment.getTimestamp(), e.getLineNumber());

		try {
			return getResultCollector().storeEventResult(r);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean startProcessing(Experiment e) {
		if (e != null) {
			this.experiment = e;
			er = new TestExperimentResultEvent(e.getInputFileName(),
					e.getOutputFileName(), FileManagerImpl.LOG_PATH
							+ e.getTimestamp(), e.getName());
			return true;
		} else
			return false;

	}

	@Override
	public Experiment stopProcessing() {
		er.setTimestamp_end(System.currentTimeMillis());
		resultCollector.storeExperimentResult(er);
		return experiment;
	}

	@Override
	public void turnOn() {
		Logger.getRootLogger().info("Nothing to do");
	}

	@Override
	public void turnOff() {
		Logger.getRootLogger().info("Nothing to do");
	}

}
