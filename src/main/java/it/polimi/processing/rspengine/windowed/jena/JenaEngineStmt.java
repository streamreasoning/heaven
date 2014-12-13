package it.polimi.processing.rspengine.windowed.jena;

import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.windowed.jena.abstracts.JenaEngine;
import it.polimi.processing.rspengine.windowed.jena.events.StatementEvent;
import it.polimi.processing.workbench.core.EventProcessor;
import it.polimi.utils.RDFSUtils;

import com.espertech.esper.client.UpdateListener;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

public class JenaEngineStmt extends JenaEngine {

	public JenaEngineStmt(String name, EventProcessor<Event> collector, UpdateListener listener) {
		super(name, collector, listener, StatementEvent.class);
	}

	@Override
	protected void handleEvent(RSPEvent e) {
		for (TripleContainer tc : e.getEventTriples()) {
			String[] t = tc.getTriple();
			esperEventsNumber++;
			cepRT.sendEvent(new StatementEvent(createStatement(t), cepRT.getCurrentTime(), System.currentTimeMillis()));
		}
	}

	private Statement createStatement(String[] eventTriple) {
		Resource subject = ResourceFactory.createResource(eventTriple[0]);
		Property predicate = (eventTriple[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(eventTriple[1]) : RDF.type;
		RDFNode object = ResourceFactory.createResource(eventTriple[2]);
		return ResourceFactory.createStatement(subject, predicate, object);
	}

}
