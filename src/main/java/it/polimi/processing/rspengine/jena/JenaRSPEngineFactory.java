package it.polimi.processing.rspengine.jena;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.events.CTEvent;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.rspengine.jena.enums.OntoLanguage;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.JenaEngineGraphInc;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.JenaEngineSerializedInc;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.JenaEngineStmtInc;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.JenaEngineGraph;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.JenaEngineSerialized;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.JenaEngineStmt;
import it.polimi.processing.teststand.core.TestStand;
import it.polimi.services.system.GetPropertyValues;

import com.espertech.esper.client.UpdateListener;

public final class JenaRSPEngineFactory {

	public static RSPEngine getSerializedEngine(EventProcessor<CTEvent> next, UpdateListener listener) {
		return new JenaEngineSerialized(GetPropertyValues.getEnumProperty(OntoLanguage.class, "onto_lang").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}

	public static RSPEngine getStmtEngine(TestStand next, UpdateListener listener) {
		return new JenaEngineStmt(GetPropertyValues.getEnumProperty(OntoLanguage.class, "onto_lang").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}

	public static RSPEngine getJenaEngineGraph(TestStand next, UpdateListener listener) {
		return new JenaEngineGraph(GetPropertyValues.getEnumProperty(OntoLanguage.class, "onto_lang").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}

	public static RSPEngine getIncrementalSerializedEngine(EventProcessor<CTEvent> next, UpdateListener listener) {
		return new JenaEngineSerializedInc(GetPropertyValues.getEnumProperty(OntoLanguage.class, "onto_lang").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}

	public static RSPEngine getIncrementalStmtEngine(TestStand next, UpdateListener listener) {
		return new JenaEngineStmtInc(GetPropertyValues.getEnumProperty(OntoLanguage.class, "onto_lang").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}

	public static RSPEngine getIncrementalJenaEngineGraph(TestStand next, UpdateListener listener) {
		return new JenaEngineGraphInc(GetPropertyValues.getEnumProperty(OntoLanguage.class, "onto_lang").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}
}
