package it.polimi.processing.rspengine.windowed.jena.listener.plain;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.interfaces.EventResult;

import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

public class JenaRhoDFListener extends JenaCEPListener {

	private final String aBoxRuleset;

	public JenaRhoDFListener(String tbox, String aBoxRuleset, ResultCollector<EventResult> collector) {
		super(tbox, collector);
		this.aBoxRuleset = aBoxRuleset;
	}

	@Override
	protected Reasoner getReasoner() {
		return new GenericRuleReasoner(Rule.rulesFromURL(aBoxRuleset));
	}
}