package it.polimi.processing.teststand.core;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.CTEvent;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.results.TSResult;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.teststand.collector.TSResultCollector;
import it.polimi.processing.teststand.streamer.TSStreamer;
import lombok.extern.log4j.Log4j;

@Log4j
public abstract class TestStand implements EventProcessor<CTEvent>, Startable<ExecutionState> {

	protected TSResultCollector collector;
	protected RSPEngine engine;
	protected TSStreamer streamer;

	protected Experiment currentExperiment;
	protected TSResult currentResult, aboxResult;

	protected int eventNumber, tsResultEvents = 0;

	protected int resultEvent;
	protected int rspEvent;
	protected int totalEvent;

	protected ExecutionState status = ExecutionState.NOT_READY;

	protected boolean isStartable() {
		return ExecutionState.NOT_READY.equals(status) || ExecutionState.CLOSED.equals(status);
	}

	protected boolean isOn() {
		return ExecutionState.READY.equals(status);
	}

	protected boolean isReady() {
		return ExecutionState.READY.equals(status);
	}

	public ExecutionState stop() {
		status = ExecutionState.CLOSED;
		return status;

	}

	public void build(TSStreamer rspEventStreamer, RSPEngine rspEngine, TSResultCollector resultCollector) {
		this.collector = resultCollector;
		this.engine = rspEngine;
		this.streamer = rspEventStreamer;
	}

	@Override
	public ExecutionState init() {
		if (!isStartable()) {
			throw new WrongStatusTransitionException("Can't Switch from Status [" + status + "] to [" + ExecutionState.READY + "]");
		} else {
			ExecutionState streamerStatus = streamer.init();
			ExecutionState engineStatus = engine.init();
			ExecutionState collectorStatus = collector.init();
			if (ExecutionState.READY.equals(streamerStatus) && ExecutionState.READY.equals(collectorStatus)
					&& ExecutionState.READY.equals(engineStatus)) {
				status = ExecutionState.READY;
				log.debug("Status [" + status + "] Initializing the TestStand");
			} else {
				log.error("RSPEventStreamerStatus [" + streamerStatus + "] collectorStatus [" + collectorStatus + "] engineStatus [" + engineStatus
						+ "]");
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
			ExecutionState rspEventStreamerStatus = streamer.close();
			ExecutionState engineStatus = engine.close();
			ExecutionState collectorStatus = collector.close();

			if (ExecutionState.CLOSED.equals(rspEventStreamerStatus) && ExecutionState.CLOSED.equals(collectorStatus)
					&& ExecutionState.CLOSED.equals(engineStatus)) {
				status = ExecutionState.CLOSED;
				log.debug("Status [" + status + "] Closing the TestStand");
			} else {
				log.error("RSPEventStreamerStatus: " + rspEventStreamerStatus);
				log.error("collectorStatus: " + collectorStatus);
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
