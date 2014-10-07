package it.polimi.teststand.engine.esper;

import it.polimi.events.Experiment;
import it.polimi.output.result.ResultCollector;
import it.polimi.teststand.engine.RSPEngine;
import it.polimi.teststand.events.TestExperimentResultEvent;
import it.polimi.teststand.events.TestResultEvent;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.time.CurrentTimeEvent;

public abstract class RSPEsperEngine extends RSPEngine {

	protected static Configuration cepConfig;
	protected static EPServiceProvider cep;
	protected static EPRuntime cepRT;
	protected static EPAdministrator cepAdm;
	protected static ConfigurationMethodRef ref;

	protected ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector;
	protected TestExperimentResultEvent er;
	protected Experiment experiment;

	protected String name;

	protected static int time = 0;

	public RSPEsperEngine(
			ResultCollector<TestResultEvent, TestExperimentResultEvent> storeSystem) {
		super(storeSystem);
	}

	public String getName() {
		return name;
	}

	protected void sendTimeEvent() {
		time += 1000;
		cepRT.sendEvent(new CurrentTimeEvent(time));
	}


	public abstract boolean startProcessing(Experiment e);

	public abstract Experiment stopProcessing();

	public abstract void turnOn();

	public abstract void turnOff();

	public ResultCollector<TestResultEvent, TestExperimentResultEvent> getResultCollector() {
		return resultCollector;
	}

	public void setResultCollector(
			ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector) {
		this.resultCollector = resultCollector;
	}

}
