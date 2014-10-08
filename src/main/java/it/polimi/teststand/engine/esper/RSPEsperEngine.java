package it.polimi.teststand.engine.esper;

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

	protected static int time = 0;

	public RSPEsperEngine(
			ResultCollector<TestResultEvent, TestExperimentResultEvent> storeSystem) {
		super(storeSystem);}

	protected void sendTimeEvent() {
		time += 1000;
		cepRT.sendEvent(new CurrentTimeEvent(time));
	}

}
