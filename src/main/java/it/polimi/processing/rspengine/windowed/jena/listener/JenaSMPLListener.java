package it.polimi.processing.rspengine.windowed.jena.listener;

import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.workbench.core.EventProcessor;

import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class JenaSMPLListener extends JenaCEPListener {

	public JenaSMPLListener(String tbox, EventProcessor<Event> collector) {
		super(tbox, collector);
	}

	@Override
	protected Reasoner getReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
		return reasoner;
	}

}