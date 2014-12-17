package it.polimi.processing.rspengine.windowed.jena;

import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.windowed.jena.abstracts.JenaEngine;
import it.polimi.processing.rspengine.windowed.jena.events.StatementEvent;
import it.polimi.processing.rspengine.windowed.jena.events.TripleEvent;
import it.polimi.processing.workbench.core.EventProcessor;
import it.polimi.utils.RDFSUtils;

import com.espertech.esper.client.UpdateListener;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;

public class JenaEngineTriple extends JenaEngine {

	public JenaEngineTriple(String name, EventProcessor<Event> collector, UpdateListener listener) {
		super(name, collector, listener, StatementEvent.class);
	}

	@Override
	protected void handleEvent(RSPTripleSet e) {

		super.handleEvent(e);

		for (TripleContainer tc : e.getEventTriples()) {
			String[] t = tc.getTriple();
			Resource subject = ResourceFactory.createResource(t[0]);
			Property predicate = (t[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(t[1]) : RDF.type;
			RDFNode object = ResourceFactory.createResource(t[2]);
			esperEventsNumber++;
			cepRT.sendEvent(new TripleEvent(subject, predicate, object, cepRT.getCurrentTime(), System.currentTimeMillis()));
		}

	}

}
