package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper;

import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.TestStandEvent;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.rspengine.esper.RSPEsperEngine;
import it.polimi.processing.rspengine.esper.commons.listener.ResultCollectorListener;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.Queries;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper.events.RDFS3;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper.events.RDFS9;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper.events.RDFSInput;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.esper.events.RDFSOut;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties.TypeOf;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFProperty;
import it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs.RDFResource;
import it.polimi.processing.teststand.core.TestStand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.log4j.Log4j;

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
@Log4j
public class NoGenericsPropertySubclass extends RSPEsperEngine {
	private final Map<String, Class<? extends RDFResource>> classes = new HashMap<String, Class<? extends RDFResource>>();
	private final Map<String, RDFProperty> props = new HashMap<String, RDFProperty>();
	private ResultCollectorListener listener = null;

	public NoGenericsPropertySubclass(String name, TestStand<RSPEngine> stand) {
		super(name, stand);
		super.stand = stand;
	}

	protected void initQueries() {
		cepAdm.createEPL(Queries.INPUT_NOGENERICS);
		cepAdm.createEPL(Queries.RDFS3_NOGENERICS);
		cepAdm.createEPL(Queries.RDFS9_NOGENERICS);
		cepAdm.createEPL(Queries.QUERYOUT_NOGENERICS);
		EPStatement out = cepAdm.createEPL("insert into Out select * from QueryOut.win:time_batch(1000 msec)");
		listener = new ResultCollectorListener(collector, this, stand.getCurrentExperiment(), new HashSet<String[]>(), new HashSet<String[]>(), null);
		out.addListener(listener);
	}

	@Override
	public boolean sendEvent(TestStandEvent e) {
		this.currentStreamingEvent = e;
		RDFSInput event = new RDFSInput();
		RDFResource s, o;
		String[] coreT = new String[3];
		List<String[]> types = new ArrayList<String[]>();
		if (e.getEventTriples().size() != e.getTripleGraph()) {
			throw new RuntimeException("mismatch on triple graph are not allowed");
		}
		for (String[] eventTriple : e.getEventTriples()) {
			String prop = eventTriple[1].replace("<", "").replace(">", "").toLowerCase().split("#")[1];
			if (!"type".equals(prop)) {
				event.setP(props.get(prop));
				coreT = eventTriple;
			} else {
				types.add(eventTriple);
			}
		}

		try {
			for (String[] ett : types) {
				Class<? extends RDFResource> domainOrRange = classes.get(ett[2].replace("<", "").replace(">", "").toLowerCase().split("#")[1]);
				if (domainOrRange == null) {
					throw new PropertyNotFoundException(ett[2] + "+is not present in system properties map");
				} else if (ett[0].equals(coreT[0])) {
					s = domainOrRange.newInstance();
					s.setValue(coreT[0]);
					event.setS(domainOrRange.cast(s));
				} else if (ett[0].equals(coreT[2])) {
					o = domainOrRange.newInstance();
					o.setValue(coreT[2]);
					event.setO(domainOrRange.cast(o));
				} else {
					throw new RuntimeException("statements are not allined " + e.getLineNumbers());
				}
			}
			event.setChannel("Input");
			check(event, e);
			event.setTimestamp(cepRT.getCurrentTime());
			cepRT.sendEvent(event);
			sendTimeEvent();
			return true;
		} catch (InstantiationException e1) {
			log.error(e1.getMessage());
		} catch (IllegalAccessException e1) {
			log.error(e1.getMessage());
		}
		return false;

	}

	private void check(RDFSInput event, TestStandEvent se) {
		if (event.getO() == null || event.getS() == null || event.getP() == null) {
			log.info(event.toString());
			log.info(se.toString());
			throw new RuntimeException("Resources must be not null, line " + se.getLineNumbers());
		}

	}

	@Override
	public ExecutionStates init() {
		ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();
		cepConfig.addMethodRef(NoGenericsPropertySubclass.class, ref);

		try {
			classRegistration();
		} catch (InstantiationException e) {
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
		}

		cepConfig.getEngineDefaults().getViewResources().setShareViews(false);
		cepConfig.getEngineDefaults().getThreading().setInternalTimerEnabled(false);

		cepConfig.addEventType("RDFSInput", RDFSInput.class.getName());
		cepConfig.addEventType("RDFS3Input", RDFS3.class.getName());
		cepConfig.addEventType("RDFS9Input", RDFS9.class.getName());
		cepConfig.addEventType("QueryOut", RDFSOut.class.getName());

		cep = EPServiceProviderManager.getProvider(NoGenericsPropertySubclass.class.getName(), cepConfig);
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		initQueries();
		return status = ExecutionStates.READY;
	}

	private void classRegistration() throws InstantiationException, IllegalAccessException {
		Reflections reflections = new Reflections("it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.properties");

		Set<Class<? extends RDFProperty>> allpropClasses = reflections.getSubTypesOf(RDFProperty.class);

		for (Class<? extends RDFProperty> class1 : allpropClasses) {
			RDFProperty c = class1.newInstance();
			cepConfig.addVariable(class1.getSimpleName().toLowerCase(), class1, class1.cast(c), true);
			props.put(class1.getSimpleName().toLowerCase(), class1.cast(c));

		}
		cepConfig.addVariable("typeOf", TypeOf.class, new TypeOf(), true);
		props.put("type", new TypeOf());

		reflections = new Reflections("it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes");
		Set<Class<? extends RDFResource>> allClasses = reflections.getSubTypesOf(RDFResource.class);

		for (Class<? extends RDFResource> class1 : allClasses) {
			classes.put(class1.getSimpleName().toLowerCase(), class1);
		}
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
		log.info("Nothing to do...Turing Off");
		return status = ExecutionStates.CLOSED;
	}
}
