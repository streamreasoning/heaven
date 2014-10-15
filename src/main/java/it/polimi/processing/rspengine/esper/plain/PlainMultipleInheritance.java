package it.polimi.processing.rspengine.esper.plain;

import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.rspengine.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.esper.commons.listener.ResultCollectorListener;
import it.polimi.processing.rspengine.esper.plain.events.Out;
import it.polimi.processing.rspengine.esper.plain.events.TEvent;
import it.polimi.processing.teststand.core.TestStand;
import it.polimi.utils.RDFSUtils;

import java.util.Set;

import org.apache.log4j.Logger;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.core.service.EPServiceProviderSPI;

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

public class PlainMultipleInheritance extends RSPEsperEngine {

	// TODO useless private static final Ontology ontology = new Ontology();

	public PlainMultipleInheritance(TestStand<RSPEngine> stand) {
		super(stand);
		super.stand = stand;
		this.name = "plain";
	}

	protected void initQueries() {

		Logger.getRootLogger().debug(Queries.input);
		Logger.getRootLogger().debug(Queries.rdfs3);
		Logger.getRootLogger().debug(Queries.rdfs9);

		cepAdm.createEPL(Queries.input);
		cepAdm.createEPL(Queries.rdfs3);
		cepAdm.createEPL(Queries.rdfs9);

		EPStatement out = cepAdm
				.createEPL("insert into Out select * from QueryOut.win:time_batch(1000 msec)");
		out.addListener(new ResultCollectorListener(collector, this));
	}

	@Override
	public ExecutionStates init() {
		ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();
		cepConfig.addMethodRef(Ontology.class, ref);

		cepConfig.addEventType("TEvent", TEvent.class.getName());
		cepConfig.addEventType("Out", Out.class.getName());

		cepConfig.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cep = (EPServiceProviderSPI) EPServiceProviderManager.getProvider(
				PlainMultipleInheritance.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		initQueries();

		return status = ExecutionStates.READY;
	}

	@Override
	public ExecutionStates startProcessing() {
		if (isStartable()) {
			resetTime();
			cepRT.sendEvent(new CurrentTimeEvent(time));
			return status = ExecutionStates.READY;
		} else
			return status = ExecutionStates.ERROR;
	}

	private void resetTime() {
		time = 0;
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		// TODO if can send
		this.currentStreamingEvent = e;
		status = ExecutionStates.RUNNING;
		TEvent esperEvent;
		Set<String[]> eventTriples = e.getEventTriples();
		for (String[] eventTriple : eventTriples) {
			if (!RDFSUtils.TYPE_PROPERTY.equals(eventTriple[1])) { // Discart
																	// typeof
																	// triples
				Logger.getRootLogger().info("Create New Esper Event");
				esperEvent = new TEvent(new String[] { eventTriple[0] },
						eventTriple[1], new String[] { eventTriple[2] },
						"Input", cepRT.getCurrentTime());
				cepRT.sendEvent(esperEvent);
			}
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

	public boolean isStartable() {
		return ExecutionStates.READY.equals(status)
				|| ExecutionStates.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionStates.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionStates.READY.equals(status);
	}
}
