package it.polimi.processing.rspengine.esper.plain;

import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.rspengine.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.esper.commons.listener.ResultCollectorListener;
import it.polimi.processing.rspengine.esper.plain.events.Out;
import it.polimi.processing.rspengine.esper.plain.events.TEvent;
import it.polimi.processing.teststand.core.TestStand;

import java.util.Set;

import org.apache.log4j.Logger;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
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

public class PlainCompleteRHODF extends RSPEsperEngine {

	private ResultCollectorListener listener = null;

	public PlainCompleteRHODF(String name, TestStand<RSPEngine> stand) {
		super(name, stand);
		super.stand = stand;
	}

	protected void initQueries() {

		Logger.getRootLogger().debug(Queries.input);
		Logger.getRootLogger().debug(Queries.rdfs3);
		Logger.getRootLogger().debug(Queries.rdfs6);
		Logger.getRootLogger().debug(Queries.rdfs9);

		cepAdm.createEPL(Queries.input);
		cepAdm.createEPL(Queries.rdfs3);
		cepAdm.createEPL(Queries.rdfs6);
		cepAdm.createEPL(Queries.rdfs9);

		EPStatement out = cepAdm.createEPL("insert into Out select * from QueryOut.win:time_batch(1000 msec)");
		listener = new ResultCollectorListener(collector, this, stand.getCurrentExperiment());
		out.addListener(listener);
	}

	@Override
	public ExecutionStates init() {
		ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();
		cepConfig.addMethodRef(Ontology.class, ref);

		cepConfig.addEventType("TEvent", TEvent.class.getName());
		cepConfig.addEventType("Out", Out.class.getName());

		cepConfig.getEngineDefaults().getThreading().setInternalTimerEnabled(false);

		cep = EPServiceProviderManager.getProvider(PlainCompleteRHODF.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		Ontology.init();

		initQueries();

		return status = ExecutionStates.READY;
	}

	@Override
	public ExecutionStates startProcessing() {
		if (isStartable()) {
			resetTime();
			listener.setExperiment(stand.getCurrentExperiment());
			cepRT.sendEvent(new CurrentTimeEvent(time));
			return status = ExecutionStates.READY;
		} else
			return status = ExecutionStates.ERROR;
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		this.currentStreamingEvent = e;
		status = ExecutionStates.RUNNING;
		TEvent esperEvent;
		Set<String[]> eventTriples = e.getEventTriples();

		Logger.getLogger("obqa").debug(eventTriples);
		for (String[] eventTriple : eventTriples) {
			Logger.getRootLogger().debug("Create New Esper Event");
			Logger.getRootLogger().debug(eventTriple[1]);
			esperEvent = new TEvent(eventTriple[0], eventTriple[1], eventTriple[2], cepRT.getCurrentTime(), System.currentTimeMillis(), "Input");
			cepRT.sendEvent(esperEvent);
		}
		sendTimeEvent();
		status = ExecutionStates.READY;
		return true;
	}

	@Override
	public ExecutionStates stopProcessing() {
		if (isOn()) {
			return status = ExecutionStates.CLOSED;
		} else
			return status = ExecutionStates.ERROR;
	}

	@Override
	public ExecutionStates close() {
		Logger.getRootLogger().info("Nothing to do...Turing Off");
		return status = ExecutionStates.CLOSED;
	}

}
