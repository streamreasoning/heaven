package it.polimi.heaven.baselines.esper;

import it.polimi.heaven.core.enums.ExecutionState;
import it.polimi.heaven.core.ts.EventProcessor;
import it.polimi.heaven.core.ts.events.Stimulus;
import it.polimi.heaven.core.ts.rspengine.RSPEngine;
import lombok.Getter;
import lombok.Setter;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;

@Getter
public abstract class RSPEsperEngine implements RSPEngine {

	protected Configuration cepConfig;
	protected EPServiceProvider cep;
	protected EPRuntime cepRT;
	protected EPAdministrator cepAdm;
	protected ConfigurationMethodRef ref;

	protected ExecutionState status;
	protected EventProcessor<Stimulus> next;

	@Setter
	protected Stimulus currentEvent = null;
	protected long sentTimestamp;

	protected int rspEventsNumber = 0, esperEventsNumber = 0;
	protected long currentTimestamp;

	public RSPEsperEngine(EventProcessor<Stimulus> next, Configuration config) {
		this.next = next;
		this.cepConfig = config;
		this.currentTimestamp = 0L;
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