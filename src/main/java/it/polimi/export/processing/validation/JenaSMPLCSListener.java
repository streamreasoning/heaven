package it.polimi.export.processing.validation;

import it.polimi.processing.ets.core.EventProcessor;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.results.CSResult;

import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class JenaSMPLCSListener extends CSListener {

	public JenaSMPLCSListener(String tbox, EventProcessor<Event> collector) {
		super(tbox, collector);
	}

	@Override
	protected Reasoner getReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
		return reasoner;
	}

	@Override
	protected void sendResult() {
		boolean abox = true;
		collector.process(new CSResult("", statements, eventNumber, ouputcurrentTimeMillis, !abox, completeness, soundness, null, null));
		collector.process(new CSResult("", ABoxTriples, eventNumber, ouputcurrentTimeMillis, abox, completeness, soundness, null, null));

	}

}
