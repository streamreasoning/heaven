package it.polimi.processing.rspengine.windowed.jena;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.windowed.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.windowed.esper.plain.events.TEvent;

import java.util.Set;

import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;

/**
 * In this example rdfs property of subclass of is exploited by external static
 * functions which can be called form EPL No data or time windows are considered
 * in event consuming, se the related example for that time is externally
 * controlled all event are sent in the samte time interval
 * 
 * the query doesn't include joins
 * 
 * events are pushed, on incoming events, in 3 differents queue which are pulled
 * by refering statements
 * 
 * **/
@Log4j
public class JenaEngine extends RSPEsperEngine {

	private final UpdateListener listener;
	private final Class<?> eventClass;

	public JenaEngine(String name, ResultCollector<EventResult> collector, UpdateListener listener, Class<?> eventClass) {
		super(name, collector);
		this.listener = listener;
		this.eventClass = eventClass;
	}

	public JenaEngine(String name, ResultCollector<EventResult> collector, UpdateListener listener) {
		super(name, collector);
		this.listener = listener;
		this.eventClass = TEvent.class;
	}

	protected void initQueries() {

		EPStatement out = cepAdm.createEPL("insert into Out select * from TEvent.win:time(1000 msec) output all every 1000msec ");
		out.addListener(listener);
	}

	@Override
	public ExecutionState init() {
		ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();

		cepConfig.addEventType("TEvent", eventClass.getName());

		cepConfig.getEngineDefaults().getThreading().setInternalTimerEnabled(false);

		cep = EPServiceProviderManager.getProvider(JenaEngine.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();
		initQueries();
		status = ExecutionState.READY;
		log.debug("Status[" + status + "] Initizalized the RSPEngine");
		return status;
	}

	@Override
	public ExecutionState startProcessing() {
		if (isStartable()) {
			resetTime();
			cepRT.sendEvent(new CurrentTimeEvent(time));
			status = ExecutionState.READY;
		} else {
			status = ExecutionState.ERROR;
		}
		return status;
	}

	@Override
	public boolean process(RSPEvent e) {
		setCurrentEvent(e);
		status = ExecutionState.RUNNING;
		Set<TripleContainer> eventTriples = e.getEventTriples();
		int count = 0;
		for (TripleContainer tc : eventTriples) {
			count++;
			if (count % 1000 == 0) {
				log.debug("Sent [" + count + "] events");
			}
			// Esper events must be immutable
			String[] t = tc.getTriple();
			cepRT.sendEvent(new TEvent(t[0], t[1], t[2], cepRT.getCurrentTime(), System.currentTimeMillis(), "Input"));
		}
		log.debug("Status[" + status + "] Parsing done, prepare time scheduling...");
		status = ExecutionState.READY;
		return true;
	}

	@Override
	public ExecutionState stopProcessing() {
		if (isOn()) {
			status = ExecutionState.CLOSED;
		} else {
			status = ExecutionState.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionState close() {
		status = ExecutionState.CLOSED;
		log.info("Status[" + status + "] Nothing to do...Turing Off");
		return status;
	}

	@Override
	public boolean processDone() {
		status = ExecutionState.READY;
		return true;
	}
}
