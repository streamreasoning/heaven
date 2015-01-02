package it.polimi.processing.ets.core;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.events.results.EventResult;
import it.polimi.processing.events.results.TSResult;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.streamer.RSPTripleSetStreamer;
import lombok.extern.log4j.Log4j;

@Log4j
public abstract class TestStandImpl extends Stand implements EventProcessor<Event>, Startable<ExecutionState> {

	protected StartableCollector<EventResult> resultCollector;
	protected StartableCollector<ExperimentResult> experimentResultCollector;

	protected RSPEngine rspEngine;
	protected RSPTripleSetStreamer rspEventStreamer;

	protected Experiment currentExperiment;
	protected Event se;
	protected TSResult currentResult;

	protected int eventNumber, tsResultEvents = 0;

	protected int resultEvent;
	protected int rspEvent;
	protected int totalEvent;

	public void build(StartableCollector<EventResult> resultCollector, StartableCollector<ExperimentResult> experimentResultCollector,
			RSPEngine rspEngine, RSPTripleSetStreamer rspEventStreamer) {
		this.experimentResultCollector = experimentResultCollector;
		this.resultCollector = resultCollector;
		this.rspEngine = rspEngine;
		this.rspEventStreamer = rspEventStreamer;
	}

	@Override
	public ExecutionState init() {
		if (!isStartable()) {
			throw new WrongStatusTransitionException("Can't Switch from Status [" + status + "] to [" + ExecutionState.READY + "]");
		} else {
			ExecutionState streamerStatus = rspEventStreamer.init();
			ExecutionState engineStatus = rspEngine.init();
			ExecutionState collectorStatus = resultCollector.init();
			ExecutionState experimenTcollectorStatus = experimentResultCollector.init();
			if (ExecutionState.READY.equals(streamerStatus) && ExecutionState.READY.equals(collectorStatus)
					&& ExecutionState.READY.equals(engineStatus) && ExecutionState.READY.equals(experimenTcollectorStatus)) {
				status = ExecutionState.READY;
				log.debug("Status [" + status + "] Initializing the TestStand");
			} else {
				log.error("RSPEventStreamerStatus [" + streamerStatus + "] collectorStatus [" + collectorStatus + "] experimentCollectorStatus ["
						+ experimenTcollectorStatus + "] engineStatus [" + engineStatus + "]");
				status = ExecutionState.ERROR;
			}
			return status;
		}

	}

	@Override
	public ExecutionState close() {
		if (!isOn()) {
			throw new WrongStatusTransitionException("Can't Switch from Status [" + status + "] to [" + ExecutionState.CLOSED + "]");
		} else {
			ExecutionState rspEventStreamerStatus = rspEventStreamer.close();
			ExecutionState engineStatus = rspEngine.close();
			ExecutionState collectorStatus = resultCollector.close();
			ExecutionState experimenTcollectorStatus = experimentResultCollector.close();

			if (ExecutionState.CLOSED.equals(rspEventStreamerStatus) && ExecutionState.CLOSED.equals(experimenTcollectorStatus)
					&& ExecutionState.CLOSED.equals(collectorStatus) && ExecutionState.CLOSED.equals(engineStatus)) {
				status = ExecutionState.CLOSED;
				log.debug("Status [" + status + "] Closing the TestStand");
			} else {
				log.error("RSPEventStreamerStatus: " + rspEventStreamerStatus);
				log.error("collectorStatus: " + collectorStatus);
				log.error("experimentCollectorStatus: " + experimenTcollectorStatus);
				log.error("engineStatus: " + engineStatus);
				status = ExecutionState.ERROR;
			}

			log.info("Status [" + status + "] Processed RSPEvents [" + rspEvent + "]  ResultEvents [" + resultEvent + "]  Total [" + totalEvent
					+ "] Produced [" + tsResultEvents + "] TSResult Events");
			return status;
		}
	}

	public abstract int run(Experiment e);

	public abstract int run(Experiment e, String comment);
}
