package it.polimi.processing.rspengine.jena;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.ets.core.RSPTestStand;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.rspengine.jena.enums.Reasoner;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.JenaEngineGraphInc;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.JenaEngineSerializedInc;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.JenaEngineStmtInc;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.JenaEngineGraph;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.JenaEngineSerialized;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.JenaEngineStmt;
import it.polimi.services.system.GetPropertyValues;

import com.espertech.esper.client.UpdateListener;

public final class JenaRSPEngineFactory {

	public static RSPEngine getSerializedEngine(EventProcessor<Event> next, UpdateListener listener) {
		return new JenaEngineSerialized(GetPropertyValues.getEnumProperty(Reasoner.class, "jena_current_reasoner").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}

	public static RSPEngine getStmtEngine(RSPTestStand next, UpdateListener listener) {
		return new JenaEngineStmt(GetPropertyValues.getEnumProperty(Reasoner.class, "jena_current_reasoner").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}

	public static RSPEngine getJenaEngineGraph(RSPTestStand next, UpdateListener listener) {
		return new JenaEngineGraph(GetPropertyValues.getEnumProperty(Reasoner.class, "jena_current_reasoner").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}

	public static RSPEngine getIncrementalSerializedEngine(EventProcessor<Event> next, UpdateListener listener) {
		return new JenaEngineSerializedInc(GetPropertyValues.getEnumProperty(Reasoner.class, "jena_current_reasoner").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}

	public static RSPEngine getIncrementalStmtEngine(RSPTestStand next, UpdateListener listener) {
		return new JenaEngineStmtInc(GetPropertyValues.getEnumProperty(Reasoner.class, "jena_current_reasoner").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}

	public static RSPEngine getIncrementalJenaEngineGraph(RSPTestStand next, UpdateListener listener) {
		return new JenaEngineGraphInc(GetPropertyValues.getEnumProperty(Reasoner.class, "jena_current_reasoner").name().toLowerCase(), next,
				listener != null ? listener : JenaReasoningListenerFactory.getCurrent());
	}
}
