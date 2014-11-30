package it.polimi.processing.validation;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.Result;
import it.polimi.processing.events.interfaces.EventResult;

import java.io.IOException;

import com.hp.hpl.jena.rdf.model.impl.InfModelImpl;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class CompleteSoundListener extends CSListener {

	private boolean cs, ss, cr, sr;
	private final String aBoxRuleset;

	public CompleteSoundListener(String tbox, String aBoxRuleset, ResultCollector<EventResult> collector) {
		super(tbox, collector);
		this.aBoxRuleset = aBoxRuleset;
	}

	@Override
	protected void sendResult() throws IOException {
		collector
				.store(new Result(statements, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis(), cs, ss, cr, sr), "Result");
		collector.store(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis(), cs, ss, cr, sr),
				"Window");
	}

	@Override
	protected Reasoner getReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
		return reasoner;
	}

	protected Reasoner getRHODFReasoner() {
		return new GenericRuleReasoner(Rule.rulesFromURL(aBoxRuleset));
	}

	@Override
	protected void evaluation() {
		Reasoner smpl = getReasoner();
		InfGraph graph = smpl.bindSchema(TBoxStar.getGraph()).bind(jenaABox.getGraph());
		jenaResult = new InfModelImpl(graph);
		cs = Analyser.isComplete(jenaResult, esperResult);
		ss = Analyser.isSound(jenaResult, esperResult);
		Reasoner rhodf = getRHODFReasoner();
		graph = rhodf.bindSchema(TBoxStar.getGraph()).bind(jenaABox.getGraph());
		jenaResult = new InfModelImpl(graph);
		cr = Analyser.isComplete(jenaResult, esperResult);
		sr = Analyser.isSound(jenaResult, esperResult);
	}

}
