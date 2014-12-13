package it.polimi.processing.rspengine.windowed.jena.listener;

import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.workbench.core.EventProcessor;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

public class JenaRhoDFListener extends JenaCEPListener {

	private final String aBoxRuleset;

	public JenaRhoDFListener(Model tbox, String aBoxRuleset, EventProcessor<Event> collector) {
		super(tbox, collector);
		this.aBoxRuleset = aBoxRuleset;
	}

	@Override
	protected Reasoner getReasoner() {
		return new GenericRuleReasoner(Rule.rulesFromURL(aBoxRuleset));
	}
}