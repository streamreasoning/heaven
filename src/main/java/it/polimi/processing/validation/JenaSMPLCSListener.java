package it.polimi.processing.validation;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.Result;
import it.polimi.processing.events.interfaces.EventResult;

import java.io.IOException;

import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class JenaSMPLCSListener extends CSListener {

	public JenaSMPLCSListener(String tbox, ResultCollector<EventResult> collector) {
		super(tbox, collector);
	}

	@Override
	protected Reasoner getReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
		return reasoner;
	}

	@Override
	protected void sendResult() throws IOException {
		collector.process(new Result(statements, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis(), completeness, soundness,
				null, null), "Result");
		collector.process(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis(), completeness, soundness,
				null, null), "Window");

	}

}
