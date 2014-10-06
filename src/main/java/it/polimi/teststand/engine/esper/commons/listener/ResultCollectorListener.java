package it.polimi.teststand.engine.esper.commons.listener;

import it.polimi.output.result.ResultCollector;
import it.polimi.output.result.Storable;
import it.polimi.teststand.events.Experiment;
import it.polimi.teststand.events.TestExperimentResultEvent;
import it.polimi.teststand.events.TestResultEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.espertech.esper.client.EventBean;

public class ResultCollectorListener extends GenearlListener {

	ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector;
	Experiment experiment;
	private Integer lineNumber;

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public ResultCollectorListener(ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector, Experiment e) {
		this.resultCollector = resultCollector;
		this.experiment = e;
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {

		TestResultEvent eventToSend;
		Set<String[]> statements = new HashSet<String[]>();
		Set<String[]> start_triples = new HashSet<String[]>();
		for (EventBean eventBean : newEvents) {
			Logger.getRootLogger().debug(experiment.getInputFileName() + " "
					+ eventBean.getUnderlying());
			Storable storableEvent = (Storable) eventBean.getUnderlying();

			statements.addAll(storableEvent.getTriples());
			
			if (eventBean.get("channel").equals("Input")) {
				start_triples.addAll(storableEvent.getTriples());
			}
		}

		eventToSend = new TestResultEvent(statements, start_triples,
				(long) newEvents[0].get("app_timestamp"),
				experiment.getOutputFileName(), experiment.getName(),
				experiment.getTimestamp(),lineNumber);

		try {
			Logger.getRootLogger().debug("SEND STORE EVENT");
			resultCollector.storeEventResult(eventToSend);
			Logger.getRootLogger().debug("SENT STORE EVENT");
		} catch (IOException e) {
			Logger.getRootLogger().debug("SEND NOt STORE EVENT");
		}
	}
}
