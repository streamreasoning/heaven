package it.polimi.processing.rspengine.esper;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.RSPEngine;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.time.CurrentTimeEvent;

@Getter
@Log4j
public abstract class RSPEsperEngine extends RSPEngine {

	protected static Configuration cepConfig;
	protected static EPServiceProvider cep;
	protected static EPRuntime cepRT;
	protected static EPAdministrator cepAdm;
	protected static ConfigurationMethodRef ref;
	@Getter
	protected RSPEvent currentStreamingEvent = null;

	protected int time = 0;

	public RSPEsperEngine(String name, ResultCollector<EventResult> collector) {
		super(name, collector);
	}

	protected void sendTimeEvent() {

		time += 1000;
		cepRT.sendEvent(new CurrentTimeEvent(time));
		log.debug("Sent time Event");
	}

	protected void resetTime() {
		time = 0;
	}

	protected boolean isStartable() {
		return ExecutionState.READY.equals(status) || ExecutionState.CLOSED.equals(status);
	}

	protected boolean isOn() {
		return ExecutionState.READY.equals(status);
	}

	protected boolean isReady() {
		return ExecutionState.READY.equals(status);
	}

}
