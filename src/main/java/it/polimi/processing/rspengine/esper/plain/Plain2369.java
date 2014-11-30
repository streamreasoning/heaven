package it.polimi.processing.rspengine.esper.plain;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.esper.commons.listener.ResultCollectorListener;
import it.polimi.processing.rspengine.esper.plain.events.Out;
import it.polimi.processing.rspengine.esper.plain.events.TEvent;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j;

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
@Log4j
public class Plain2369 extends RSPEsperEngine<RSPEvent> {

	private ResultCollectorListener listener = null;
	private final String runtimeOnto;
	private TEvent esperEvent = null;
	private final String ontologyClass;

	public Plain2369(String name, ResultCollector<EventResult> collector, String runtimeOnto, String ontologyClass) {
		super(name, collector);
		this.runtimeOnto = runtimeOnto;
		this.ontologyClass = ontologyClass;
	}

	protected void initQueries() {

		log.debug(Queries.INPUT);
		log.debug(Queries.RDFS23);
		log.debug(Queries.RDFS6);
		log.debug(Queries.RDFS9);
		//
		// cepAdm.createEPL(Queries.RDFS6REV);
		// cepAdm.createEPL(Queries.RDFS23REV1);
		// cepAdm.createEPL(Queries.RDFS23REV2);
		// cepAdm.createEPL(Queries.RDFS23REV3);
		// cepAdm.createEPL(Queries.RDFS23REV4);
		// cepAdm.createEPL(Queries.RDFS9REV1);
		// cepAdm.createEPL(Queries.RDFS9REV2);
		// cepAdm.createEPL(Queries.RDFS9REV3);
		// cepAdm.createEPL(Queries.OUTPUTREV1);
		// cepAdm.createEPL(Queries.OUTPUTREV3);
		// cepAdm.createEPL(Queries.OUTPUTREV4);

		cepAdm.createEPL(Queries.INPUT);
		cepAdm.createEPL(Queries.RDFS23);
		cepAdm.createEPL(Queries.RDFS6);
		cepAdm.createEPL(Queries.RDFS9);

		EPStatement out = cepAdm.createEPL("insert into Out select * from QueryOut");
		listener = new ResultCollectorListener(collector, this, name, new HashSet<String[]>(), new HashSet<String[]>(), null);
		out.addListener(listener);
	}

	@Override
	public ExecutionState init() {
		ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();

		cepConfig.addEventType("TEvent", TEvent.class.getName());
		cepConfig.addEventType("Out", Out.class.getName());

		cepConfig.getEngineDefaults().getThreading().setInternalTimerEnabled(false);

		cep = EPServiceProviderManager.getProvider(Plain2369.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		if ("CompleatingOntology".equals(ontologyClass)) {
			cepConfig.addMethodRef(CompletingOntology.class, ref);
			CompletingOntology.init(runtimeOnto);
		} else if ("CompleteOntology".equals(ontologyClass)) {
			cepConfig.addMethodRef(CompleteOntology.class, ref);
			CompleteOntology.init(runtimeOnto);
		} else {
			cepConfig.addMethodRef(Ontology.class, ref);
			Ontology.init(runtimeOnto);
		}

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
	public boolean sendEvent(RSPEvent e) {
		this.currentStreamingEvent = e;
		status = ExecutionState.RUNNING;
		Set<String[]> eventTriples = e.getEventTriples();

		int count = 0;
		for (String[] eventTriple : eventTriples) {
			count++;
			if (count % 1000 == 0) {
				log.debug("Sent [" + count + "] events");
			}
			// Esper events must be immutable
			esperEvent = new TEvent(eventTriple[0], eventTriple[1], eventTriple[2], cepRT.getCurrentTime(), System.currentTimeMillis(), "Input");
			cepRT.sendEvent(esperEvent);
		}

		log.debug("Status[" + status + "] Parsing done, prepare time scheduling...");

		sendTimeEvent();
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

}