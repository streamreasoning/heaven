package it.polimi.processing.validation;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.Result;
import it.polimi.processing.events.interfaces.EventResult;

import java.io.IOException;

import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

public class JenaRhoDFCSListener extends CSListener {

	private final String aBoxRuleset;

	public JenaRhoDFCSListener(String tbox, String aBoxRuleset, ResultCollector<EventResult> collector) {
		super(tbox, collector);
		this.aBoxRuleset = aBoxRuleset;
	}

	@Override
	protected Reasoner getReasoner() {
		return new GenericRuleReasoner(Rule.rulesFromURL(aBoxRuleset));
	}

	@Override
	protected void sendResult() throws IOException {
		collector.store(new Result(statements, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis(), null, null, completeness,
				soundness), "Result");
		collector.store(new Result(ABoxTriples, eventNumber, (eventNumber + ABoxTriples.size()), System.currentTimeMillis(), null, null,
				completeness, soundness), "Window");

	}

}
