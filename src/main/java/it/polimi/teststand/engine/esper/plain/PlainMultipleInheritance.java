package it.polimi.teststand.engine.esper.plain;

import it.polimi.output.filesystem.FileManagerImpl;
import it.polimi.output.result.ResultCollector;
import it.polimi.teststand.engine.RSPEngine;
import it.polimi.teststand.engine.esper.commons.listener.ResultCollectorListener;
import it.polimi.teststand.engine.esper.plain.events.Out;
import it.polimi.teststand.engine.esper.plain.events.TEvent;
import it.polimi.teststand.events.Event;
import it.polimi.teststand.events.Experiment;
import it.polimi.teststand.events.ExperimentResult;
import it.polimi.teststand.events.StreamingEvent;

import java.util.Set;

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
 * 
 * 
 * 
 * **/

public class PlainMultipleInheritance extends RSPEngine {

	// TODO useless private static final Ontology ontology = new Ontology();

	public Integer iterationNumber = 0;
	private ResultCollectorListener listener;

	public PlainMultipleInheritance(ResultCollector storeSystem) {
		super(storeSystem);
		this.name = "plain";
	}

	protected void initQueries() {

		_logger.debug(Queries.input);
		_logger.debug(Queries.rdfs3);
		_logger.debug(Queries.rdfs9);

		cepAdm.createEPL(Queries.input);
		cepAdm.createEPL(Queries.rdfs3);
		cepAdm.createEPL(Queries.rdfs9);

		EPStatement out = cepAdm
				.createEPL("insert into Out select * from QueryOut.win:time_batch(1000 msec)");
		out.addListener(listener = new ResultCollectorListener(resultCollector,
				experiment));
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		listener.setLineNumber(e.getLineNumber());
		TEvent esperEvent;
		Set<String[]> eventTriples = e.getEventTriples();
		for (String[] eventTriple : eventTriples) {
			_logger.debug("Create New Esper Event");
			esperEvent = new TEvent(new String[] { eventTriple[0] },
					eventTriple[1], new String[] { eventTriple[2] }, "Input",
					cepRT.getCurrentTime());
			System.out.println("Esper Event " + esperEvent.toString());
			cepRT.sendEvent(esperEvent);
		}
		sendTimeEvent();
		/*
		 * Come dovrebbe essere il flusso: arriva una tripla rdf, la passo ad
		 * esper esper procede con una query mando avanti il tempo, la query
		 * scatta e il listener riceve l'output il listener manda al result
		 * collector gli eventi relativi
		 */
		return true;
	}

	@Override
	public boolean startProcessing(Experiment e) {
		if (e != null) {
			this.experiment = e;
			cepRT.sendEvent(new CurrentTimeEvent(time));
			initQueries();
			er = new ExperimentResult(e.getInputFileName(),
					e.getOutputFileName(), FileManagerImpl.LOG_PATH
							+ e.getTimestamp());

			return true;
		} else
			return false;
	}

	@Override
	public Experiment stopProcessing() {
		er.setTimestamp_end(System.currentTimeMillis());
		resultCollector.storeExperimentResult(er);
		return experiment;
	}

	@Override
	public void turnOn() {
		initLogger();
		initEsper();
	}

	@Override
	public void turnOff() {
		System.out.println("Nothing to do");
	}

	private static void initEsper() {

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
	}

}
