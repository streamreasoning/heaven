package rdf.museo.ihneritance.nogenerics.esper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import rdf.museo.ihneritance.nogenerics.esper.events.RDFS3;
import rdf.museo.ihneritance.nogenerics.esper.events.RDFS9;
import rdf.museo.ihneritance.nogenerics.esper.events.RDFSInput;
import rdf.museo.ihneritance.nogenerics.esper.events.RDFSOut;
import rdf.museo.ihneritance.nogenerics.ontology.properties.TypeOf;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;
import rdf.museo.simple.Queries;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.core.service.EPServiceProviderSPI;
import commons.EsperModel;
import commons.LoggingListener;

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

public class NoGenericsPropertySubclass extends EsperModel {

	private static Map<String, RDFProperty> props = new HashMap<String, RDFProperty>();

	public NoGenericsPropertySubclass() throws InstantiationException, IllegalAccessException {
		initLogger();
		initEsper();
		initQueries();
		moveTime();
	}


	private static void initEsper() throws InstantiationException,
			IllegalAccessException {
		ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();
		cepConfig.addMethodRef(NoGenericsPropertySubclass.class, ref);

		// eventi in classi diverse perche' altrimenti non vengono distinti a
		// livello di ELP, indagare
		cepConfig.addEventType("RDFSInput", RDFSInput.class.getName());
		cepConfig.addEventType("RDFS3Input", RDFS3.class.getName());
		cepConfig.addEventType("RDFS9Input", RDFS9.class.getName());
		cepConfig.addEventType("QueryOut", RDFSOut.class.getName());

		Reflections reflections = new Reflections(
				"rdf.museo.ihneritance.nogenerics.ontology.properties");

		Set<Class<? extends RDFProperty>> allClasses = reflections
				.getSubTypesOf(RDFProperty.class);

		for (Class<? extends RDFProperty> class1 : allClasses) {
			RDFProperty c = class1.newInstance();
			cepConfig.addVariable(class1.getSimpleName().toLowerCase(), class1,
					class1.cast(c), true);
			props.put(class1.getSimpleName().toLowerCase(), class1.cast(c));

		}

		props.put("type", new TypeOf());

		cepConfig.getEngineDefaults().getViewResources().setShareViews(false);
		cepConfig.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cep = (EPServiceProviderSPI) EPServiceProviderManager.getProvider(
				NoGenericsPropertySubclass.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();
	}

	protected  void initQueries() {
		cepAdm.createEPL(Queries.input_nogenerics);
		cepAdm.createEPL(Queries.rdfs3_nogenerics);
		cepAdm.createEPL(Queries.rdfs9_nogenerics);
		cepAdm.createEPL(Queries.queryOut_nogenerics).addListener(
				new LoggingListener("", false, false, false, cepConfig,
						(EPServiceProviderSPI) cep, (String[]) null));
	}

	public void sendEvent(String[] eventTriple) {
		synchronized (BUSY) {
			BUSY = true;
			cepRT.sendEvent(new RDFSInput(new RDFResource(eventTriple[0]),
					props.get(eventTriple[1].toLowerCase()), new RDFResource(
							eventTriple[2]), cepRT.getCurrentTime()));
			moveTime();
			BUSY = false;
		}

	}


	public boolean isBusy() {
		return BUSY;
	}
}
