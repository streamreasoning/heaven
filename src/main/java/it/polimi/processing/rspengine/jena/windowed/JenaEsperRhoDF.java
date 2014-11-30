package it.polimi.processing.rspengine.jena.windowed;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.esper.plain.events.TEvent;

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
public class JenaEsperRhoDF extends RSPEsperEngine {

	private UpdateListener listener = null;

	public JenaEsperRhoDF(String name, ResultCollector<EventResult> collector, UpdateListener listener) {
		super(name, collector);
		this.listener = listener;
	}

	protected void initQueries() {

		EPStatement out = cepAdm.createEPL("insert into Out select * from TEvent.win:time(1000 msec) output all every 1000msec ");
		out.addListener(listener);
	}

	@Override
	public ExecutionState init() {
		ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();

		cepConfig.addEventType("TEvent", TEvent.class.getName());

		cepConfig.getEngineDefaults().getThreading().setInternalTimerEnabled(false);

		cep = EPServiceProviderManager.getProvider(JenaEsperRhoDF.class.getName(), cepConfig);
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
		Set<String[]> eventTriples = e.getEventTriples();
		int count = 0;
		for (String[] eventTriple : eventTriples) {
			count++;
			if (count % 1000 == 0) {
				log.debug("Sent [" + count + "] events");
			}
			// Esper events must be immutable
			cepRT.sendEvent(new TEvent(eventTriple[0], eventTriple[1], eventTriple[2], cepRT.getCurrentTime(), System.currentTimeMillis(), "Input"));
		}

		log.debug("Status [" + status + "] Parsing done, return...");
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
		moveWindow();
		status = ExecutionState.READY;
		return true;
	}
}
