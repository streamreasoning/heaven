package it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.listener;

import it.polimi.processing.ets.core.EventProcessor;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.listener.abstracts.JenaIncrementalListener;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.InfModelImpl;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class JenaIncFullListener extends JenaIncrementalListener {

	public JenaIncFullListener(Model tbox, EventProcessor<Event> collector) {
		super(tbox, collector);

		reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_FULL);

		InfGraph bind = reasoner.bindSchema(TBoxStar.getGraph()).bind(abox.getGraph());
		ABoxStar = new InfModelImpl(bind);
	}

}