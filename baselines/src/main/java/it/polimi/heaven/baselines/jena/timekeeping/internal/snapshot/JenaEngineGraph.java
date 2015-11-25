package it.polimi.heaven.baselines.jena.timekeeping.internal.snapshot;

import it.polimi.heaven.WindowUtils;
import it.polimi.heaven.baselines.RSPListener;
import it.polimi.heaven.baselines.jena.JenaEngine;
import it.polimi.heaven.baselines.jena.events.jena.GraphEvent;
import it.polimi.heaven.core.ts.EventProcessor;
import it.polimi.heaven.core.ts.events.Stimulus;
import it.polimi.heaven.core.ts.events.TripleContainer;
import it.polimi.utils.RDFSUtils;
import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.Configuration;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;

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
public class JenaEngineGraph extends JenaEngine {

	Graph abox;

	public JenaEngineGraph(String name, EventProcessor<Stimulus> collector, RSPListener listener) {
		super(name, collector, listener, WindowUtils.JENA_INPUT_QUERY_SNAPTSHOT);

		cepConfig = new Configuration();
		cepConfig.getEngineDefaults().getThreading().setInternalTimerEnabled(true);
		log.info("Added [" + GraphEvent.class + "] as TEvent");
		cepConfig.addEventType("TEvent", GraphEvent.class.getName());

	}

	@Override
	protected void handleEvent(Stimulus e) {
		super.handleEvent(e);
		abox = ModelFactory.createMemModelMaker().createDefaultModel().getGraph();
		for (TripleContainer tc : e.getEventTriples()) {
			String[] t = tc.getTriple();
			abox.add(createTriple(t));
		}
		esperEventsNumber++;
		cepRT.sendEvent(new GraphEvent(abox, cepRT.getCurrentTime(), System.currentTimeMillis()));

	}

	private Triple createTriple(String[] eventTriple) {
		Resource subject = ResourceFactory.createResource(eventTriple[0]);
		Property predicate = (eventTriple[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(eventTriple[1]) : RDF.type;
		RDFNode object = ResourceFactory.createResource(eventTriple[2]);
		return new Triple(subject.asNode(), predicate.asNode(), object.asNode());
	}

}