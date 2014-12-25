package it.polimi.processing.rspengine.jena;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.ets.core.RSPTestStand;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.listener.JenaIncFullListener;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.listener.JenaIncRhoDFListener;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.listener.JenaIncSMPLListener;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.listener.JenaFullListener;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.listener.JenaRhoDFListener;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.listener.JenaSMPLListener;
import it.polimi.utils.FileUtils;
import it.polimi.utils.RDFSUtils;

import com.espertech.esper.client.UpdateListener;

public final class JenaReasoningListenerFactory {

	private static UpdateListener listener;

	public static UpdateListener getSMPLListener(EventProcessor<Event> next) {
		return listener = new JenaSMPLListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RDFS_MODIFIED), next);
	}

	public static UpdateListener getRhoDfListener(EventProcessor<Event> next) {
		return listener = new JenaRhoDFListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RHODF_MODIFIED), FileUtils.RHODF_RULE_SET_RUNTIME, next);
	}

	public static UpdateListener getFULLListener(RSPTestStand next) {
		return listener = new JenaFullListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RHODF_MODIFIED), next);
	}

	public static UpdateListener getIncrementalSMPLListener(EventProcessor<Event> next) {
		return listener = new JenaIncSMPLListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RDFS_MODIFIED), next);
	}

	public static UpdateListener getIncrementalRhoDfListener(EventProcessor<Event> next) {
		return listener = new JenaIncRhoDFListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RHODF_MODIFIED), FileUtils.RHODF_RULE_SET_RUNTIME, next);
	}

	public static UpdateListener getIncrementalFULLListener(RSPTestStand next) {
		return listener = new JenaIncFullListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RHODF_MODIFIED), next);
	}

	public static UpdateListener getCurrent() {
		return listener;
	}

}
