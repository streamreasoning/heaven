package it.polimi.processing.validation;

import it.polimi.processing.events.Result;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.workbench.core.EventProcessor;

import java.io.IOException;

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
	protected void sendResult() throws IOException {
		boolean abox = true;
		collector.process(new Result(statements, eventNumber, (eventNumber + ABoxTriples.size()), ouputcurrentTimeMillis, outputmemoryUsage, null,
				null, completeness, soundness, !abox));
		collector.process(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), ouputcurrentTimeMillis, outputmemoryUsage, null,
				null, completeness, soundness, abox));

	}

}
