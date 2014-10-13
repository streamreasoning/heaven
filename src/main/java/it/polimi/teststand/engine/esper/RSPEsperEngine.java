package it.polimi.teststand.engine.esper;

import it.polimi.collector.ResultCollector;
import it.polimi.events.Event;
import it.polimi.events.result.StreamingEventResult;
import it.polimi.teststand.engine.RSPEngine;
import lombok.Getter;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.time.CurrentTimeEvent;

@Getter
public abstract class RSPEsperEngine extends RSPEngine {

	protected static Configuration cepConfig;
	protected static EPServiceProvider cep;
	protected static EPRuntime cepRT;
	protected static EPAdministrator cepAdm;
	protected static ConfigurationMethodRef ref;
	protected Event currentStreamingEvent = null;
	
	protected static int time = 0;
	
	public RSPEsperEngine(ResultCollector<StreamingEventResult> stand) {
		super(stand);
		// TODO Auto-generated constructor stub
	}


	protected void sendTimeEvent() {
		time += 1000;
		cepRT.sendEvent(new CurrentTimeEvent(time));
	}

}
