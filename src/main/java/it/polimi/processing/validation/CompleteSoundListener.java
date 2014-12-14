package it.polimi.processing.validation;

import it.polimi.processing.events.Result;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.workbench.core.EventProcessor;

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

	public CompleteSoundListener(String tbox, String aBoxRuleset, EventProcessor<Event> collector) {
		super(tbox, collector);
		this.aBoxRuleset = aBoxRuleset;
	}

	@Override
	protected void sendResult() throws IOException {
		boolean abox = true;

		collector.process(new Result(statements, eventNumber, (eventNumber + ABoxTriples.size()), ouputcurrentTimeMillis, outputmemoryUsage, cs, ss,
				cr, sr, !abox));
		collector.process(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), ouputcurrentTimeMillis, outputmemoryUsage, cs, ss,
				cr, sr, abox));
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
		cs = ModelAnalyser.isComplete(jenaResult, esperResult);
		ss = ModelAnalyser.isSound(jenaResult, esperResult);
		Reasoner rhodf = getRHODFReasoner();
		graph = rhodf.bindSchema(TBoxStar.getGraph()).bind(jenaABox.getGraph());
		jenaResult = new InfModelImpl(graph);
		cr = ModelAnalyser.isComplete(jenaResult, esperResult);
		sr = ModelAnalyser.isSound(jenaResult, esperResult);
	}

}
