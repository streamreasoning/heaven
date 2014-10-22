package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper;

import it.polimi.processing.core.TestStand;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.rspengine.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.esper.commons.listener.ResultCollectorListener;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper.events.RDFS3;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper.events.RDFS9;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper.events.RDFSInput;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper.events.RDFSOut;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties.TypeOf;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;
import it.polimi.processing.rspengine.esper.plain.Queries;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

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
 * 
 * 
 * 
 * **/

public class NoGenericsPropertySubclass extends RSPEsperEngine {
	private final Map<String, RDFProperty> props = new HashMap<String, RDFProperty>();

	public NoGenericsPropertySubclass(TestStand<RSPEngine> stand) {
		super(stand);
		super.stand = stand;
		this.name = "Nogenerics";
	}

	protected void initQueries() {
		cepAdm.createEPL(Queries.input_nogenerics);
		cepAdm.createEPL(Queries.rdfs3_nogenerics);
		cepAdm.createEPL(Queries.rdfs9_nogenerics);
		cepAdm.createEPL(Queries.queryOut_nogenerics);// TODO queries packages
		EPStatement out = cepAdm
				.createEPL("insert into Out select * from QueryOut.win:time_batch(1000 msec)");
		out.addListener(new ResultCollectorListener(collector, this)); // and
																		// listener;
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		this.currentStreamingEvent = e;
		RDFSInput event;
		for (String[] eventTriple : e.getEventTriples()) {
			String prop = eventTriple[1].replace("<", "").replace(">", "")
					.toLowerCase().split("#")[1];
			event = new RDFSInput(new RDFResource(eventTriple[0]),
					props.get(prop), new RDFResource(eventTriple[2]),
					cepRT.getCurrentTime());

			cepRT.sendEvent(event);
		}
		sendTimeEvent();
		return true;
	}

	@Override
	public ExecutionStates init() {
		ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();
		cepConfig.addMethodRef(NoGenericsPropertySubclass.class, ref);

		try {
			classRegistration();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cepConfig.getEngineDefaults().getViewResources().setShareViews(false);
		cepConfig.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cepConfig.addEventType("RDFSInput", RDFSInput.class.getName());
		cepConfig.addEventType("RDFS3Input", RDFS3.class.getName());
		cepConfig.addEventType("RDFS9Input", RDFS9.class.getName());
		cepConfig.addEventType("QueryOut", RDFSOut.class.getName());

		cep = EPServiceProviderManager.getProvider(
				NoGenericsPropertySubclass.class.getName(), cepConfig);
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		initQueries();
		return status = ExecutionStates.READY;
	}

	private void classRegistration() throws InstantiationException,
			IllegalAccessException {
		Reflections reflections = new Reflections(
				"it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties");

		Set<Class<? extends RDFProperty>> allClasses = reflections
				.getSubTypesOf(RDFProperty.class);

		for (Class<? extends RDFProperty> class1 : allClasses) {
			RDFProperty c = class1.newInstance();
			cepConfig.addVariable(class1.getSimpleName().toLowerCase(), class1,
					class1.cast(c), true);
			props.put(class1.getSimpleName().toLowerCase(), class1.cast(c));

		}
		cepConfig.addVariable("typeOf", TypeOf.class, new TypeOf(), true);
		props.put("type", new TypeOf());
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
