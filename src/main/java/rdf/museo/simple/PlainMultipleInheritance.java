package rdf.museo.simple;

import rdf.museo.simple.events.Out;
import rdf.museo.simple.events.TEvent;

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

public class PlainMultipleInheritance extends EsperModel {

	// TODO useless private static final Ontology ontology = new Ontology();

	public PlainMultipleInheritance() {
		initLogger();
		initEsper();
		initQueries();
		moveTime();
	}

	protected void initQueries() {
		cepAdm.createEPL(Queries.input);
		cepAdm.createEPL(Queries.rdfs3);
		cepAdm.createEPL(Queries.rdfs9);
		cepAdm.createEPL(Queries.queryOut).addListener(
				new LoggingListener("Queryout", false, false, false, cepConfig,
						(EPServiceProviderSPI) cep, (String[]) null));
	}

	private static void initEsper() {

		ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();
		cepConfig.addMethodRef(PlainMultipleInheritance.class, ref);

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

	public void sendEvent(String[] eventTriple) {
		synchronized (BUSY) {
			BUSY = true;
			cepRT.sendEvent(new TEvent(new String[] { eventTriple[0] },
					eventTriple[1], new String[] { eventTriple[2] }, "Input",
					cepRT.getCurrentTime()));
			moveTime();
			BUSY = false;
		}

	}

	public boolean isBusy() {
		return BUSY;
	}

}
