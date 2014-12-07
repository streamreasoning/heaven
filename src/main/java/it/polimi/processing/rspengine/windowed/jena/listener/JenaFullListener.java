package it.polimi.processing.rspengine.windowed.jena.listener;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.interfaces.EventResult;

import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class JenaFullListener extends JenaCEPListener {

	public JenaFullListener(String tbox, ResultCollector<EventResult> collector) {
		super(tbox, collector);
	}

	@Override
	protected Reasoner getReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_FULL);
		return reasoner;
	}

}