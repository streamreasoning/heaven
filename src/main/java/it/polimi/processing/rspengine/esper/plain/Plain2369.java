package it.polimi.processing.rspengine.esper.plain;

import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.rspengine.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.esper.commons.listener.ResultCollectorListener;
import it.polimi.processing.rspengine.esper.plain.events.Out;
import it.polimi.processing.rspengine.esper.plain.events.TEvent;
import it.polimi.processing.teststand.core.TestStand;

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
public class Plain2369 extends RSPEsperEngine {

	private ResultCollectorListener listener = null;
	private final String runtimeOnto;
	private TEvent esperEvent = null;
	private final String ontologyClass;

	public Plain2369(String name, TestStand<RSPEngine> stand, String runtimeOnto, String ontologyClass) {
		super(name, stand);
		super.stand = stand;
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

		EPStatement out = cepAdm.createEPL("insert into Out select * from QueryOut.win:time_batch(1000 msec)");
		listener = new ResultCollectorListener(collector, this, stand.getCurrentExperiment(), new HashSet<String[]>(), new HashSet<String[]>(), null);
		out.addListener(listener);
	}

	@Override
	public ExecutionStates init() {
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
		status = ExecutionStates.READY;
		log.debug("Status[" + status + "] Initizalized the RSPEngine");
		return status;
	}

	@Override
	public ExecutionStates startProcessing() {
		if (isStartable()) {
			resetTime();
			listener.setExperiment(stand.getCurrentExperiment());
			cepRT.sendEvent(new CurrentTimeEvent(time));
			status = ExecutionStates.READY;
		} else {
			status = ExecutionStates.ERROR;
		}
		return status;
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		this.currentStreamingEvent = e;
		status = ExecutionStates.RUNNING;
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
		status = ExecutionStates.READY;
		return true;
	}

	@Override
	public ExecutionStates stopProcessing() {
		if (isOn()) {
			status = ExecutionStates.CLOSED;
		} else {
			status = ExecutionStates.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionStates close() {
		status = ExecutionStates.CLOSED;
		log.info("Status[" + status + "] Nothing to do...Turing Off");
		return status;
	}

}
