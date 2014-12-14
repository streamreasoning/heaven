package it.polimi.processing.rspengine.windowed.jena;

import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.windowed.jena.abstracts.JenaEngine;
import it.polimi.processing.rspengine.windowed.jena.events.SerializedEvent;
import it.polimi.processing.workbench.core.EventProcessor;

import com.espertech.esper.client.UpdateListener;

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
public class JenaEngineTEvent extends JenaEngine {

	public JenaEngineTEvent(String name, EventProcessor<Event> collector, UpdateListener listener) {
		super(name, collector, listener, SerializedEvent.class);
	}

	@Override
	protected void handleEvent(RSPEvent e) {
		super.handleEvent(e);
		for (TripleContainer tc : e.getEventTriples()) {
			String[] t = tc.getTriple();
			esperEventsNumber++;
			cepRT.sendEvent(new SerializedEvent(t[0], t[1], t[2], cepRT.getCurrentTime(), System.currentTimeMillis()));
		}
	}

}
