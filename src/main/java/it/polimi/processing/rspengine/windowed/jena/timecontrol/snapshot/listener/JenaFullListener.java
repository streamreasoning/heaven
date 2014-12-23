package it.polimi.processing.rspengine.windowed.jena.timecontrol.snapshot.listener;

import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.windowed.jena.timecontrol.snapshot.listener.abstracts.JenaNaiveListener;
import it.polimi.processing.workbench.core.EventProcessor;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class JenaFullListener extends JenaNaiveListener {

	public JenaFullListener(Model tbox, EventProcessor<Event> collector) {
		super(tbox, collector);
	}

	@Override
	protected Reasoner getReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_FULL);
		return reasoner;
	}

}