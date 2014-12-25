package it.polimi.export.processing.validation;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.results.CSResult;

import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

public class JenaRhoDFCSListener extends CSListener {

	private final String aBoxRuleset;

	public JenaRhoDFCSListener(String tbox, String aBoxRuleset, EventProcessor<Event> collector) {
		super(tbox, collector);
		this.aBoxRuleset = aBoxRuleset;
	}

	@Override
	protected Reasoner getReasoner() {
		return new GenericRuleReasoner(Rule.rulesFromURL(aBoxRuleset));
	}

	@Override
	protected void sendResult() {
		boolean abox = true;
		collector.process(new CSResult("", statements, eventNumber, ouputcurrentTimeMillis, !abox, null, null, completeness, soundness));
		collector.process(new CSResult("", ABoxTriples, eventNumber, ouputcurrentTimeMillis, abox, null, null, completeness, soundness));

	}
}
