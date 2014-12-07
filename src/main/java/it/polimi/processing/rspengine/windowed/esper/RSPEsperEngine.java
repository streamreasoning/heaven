package it.polimi.processing.rspengine.windowed.esper;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.windowed.RSPEngine;
import it.polimi.processing.rspengine.windowed.esper.plain.Queries;
import it.polimi.processing.workbench.core.EventProcessor;
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

	protected int windowShots, time = 0;

	public RSPEsperEngine(String name, EventProcessor<Event> collector) {
		super(name, collector);
	}

	public void sendTimeEvent(long delta) {
		this.time += delta;
		cepRT.sendEvent(new CurrentTimeEvent(time));
		log.debug("Sent time Event");
	}

	public void moveWindow() {
		time += Queries.window;
		windowShots++;
		cepRT.sendEvent(new CurrentTimeEvent(time));
		log.debug("Sent time Event");
	}

	public void moveTimePortion(int portion) {
		time += Queries.window / portion;
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

	@Override
	public int getEventNumber() {
		return getWindowShots();
	}

	@Override
	public void progress(int i) {
		moveTimePortion(i);
	}

}
