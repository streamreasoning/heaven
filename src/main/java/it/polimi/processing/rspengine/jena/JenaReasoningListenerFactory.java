package it.polimi.processing.rspengine.jena;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.ets.core.TestStand;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.listener.JenaIncFullListener;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.listener.JenaIncRhoDFListener;
import it.polimi.processing.rspengine.jena.timekeeping.external.incremenal.listener.JenaIncSMPLListener;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.listener.JenaFullListener;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.listener.JenaRhoDFListener;
import it.polimi.processing.rspengine.jena.timekeeping.external.snapshot.listener.JenaSMPLListener;
import it.polimi.utils.FileUtils;
import it.polimi.utils.RDFSUtils;
import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.UpdateListener;

@Log4j
public final class JenaReasoningListenerFactory {

	private static UpdateListener listener;

	public static UpdateListener getSMPLListener(EventProcessor<RSPTripleSet> next) {
		log.info("Select SMPLListener ");
		return listener = new JenaSMPLListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RDFS_MODIFIED), next);
	}

	public static UpdateListener getRhoDfListener(EventProcessor<RSPTripleSet> next) {
		log.info("Select RHODFListener ");
		return listener = new JenaRhoDFListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RHODF_MODIFIED), FileUtils.RHODF_RULE_SET_RUNTIME, next);
	}

	public static UpdateListener getFULLListener(TestStand next) {
		log.info("Select FullListener ");
		return listener = new JenaFullListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RHODF_MODIFIED), next);
	}

	public static UpdateListener getIncrementalSMPLListener(EventProcessor<RSPTripleSet> next) {
		log.info("Select SMPLListener INC");
		return listener = new JenaIncSMPLListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RDFS_MODIFIED), next);
	}

	public static UpdateListener getIncrementalRhoDfListener(EventProcessor<RSPTripleSet> next) {
		log.info("Select RHODFListener INC  ");
		return listener = new JenaIncRhoDFListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RHODF_MODIFIED), FileUtils.RHODF_RULE_SET_RUNTIME, next);
	}

	public static UpdateListener getIncrementalFULLListener(TestStand next) {
		log.info("Select FullListener INC ");
		return listener = new JenaIncFullListener(RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RHODF_MODIFIED), next);
	}

	public static UpdateListener getCurrent() {
		return listener;
	}

}
