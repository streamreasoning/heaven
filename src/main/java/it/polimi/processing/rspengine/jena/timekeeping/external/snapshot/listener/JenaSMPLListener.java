package it.polimi.processing.rspengine.windowed.jena.timekeeping.external.snapshot.listener;

import it.polimi.processing.ets.core.EventProcessor;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.windowed.jena.timekeeping.external.snapshot.listener.abstracts.JenaNaiveListener;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public final class JenaSMPLListener extends JenaNaiveListener {

	public JenaSMPLListener(Model tbox, EventProcessor<Event> next) {
		super(tbox, next);
	}

	@Override
	protected Reasoner getReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
		return reasoner;
	}

}