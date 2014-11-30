package it.polimi.processing.teststand.core;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.streamer.RSPEventStreamer;
import it.polimi.utils.FileUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class TestStand extends Stand implements EventProcessor<RSPEvent>, ResultCollector<EventResult>, Startable<ExecutionState> {

	private StartableCollector<EventResult> resultCollector;
	private StartableCollector<ExperimentResult> experimentResultCollector;

	private RSPEngine rspEngine;
	private RSPEventStreamer RSPEventStreamer;
	private RSPEvent se;

	public TestStand() {
		super(ExecutionState.NOT_READY, null);
	}

	public void build(StartableCollector<EventResult> resultCollector, StartableCollector<ExperimentResult> experimentResultCollector,
			RSPEngine rspEngine, RSPEventStreamer RSPEventStreamer) {
		this.experimentResultCollector = experimentResultCollector;
		this.resultCollector = resultCollector;
		this.rspEngine = rspEngine;
		this.RSPEventStreamer = RSPEventStreamer;
	}

	public int run(String f, int experimentNumber, String comment, Date d) throws Exception {
		log.info("START STREAMING " + System.currentTimeMillis());
		String experimentDescription = "EXPERIMENT_ON_" + f + "_WITH_ENGINE_" + rspEngine.getName();
		String inputFileName = FileUtils.INPUT_FILE_PATH + f;
		DateFormat dt = new SimpleDateFormat("yyyy_MM_dd");
		String outputFileName = "Result_" + "EN" + experimentNumber + "_" + comment + "_" + dt.format(d) + "_" + f.split("\\.")[0];

		if (!isOn()) {
			throw new WrongStatusTransitionException("Not ON");
		} else {

			log.info("Status [" + status + "]" + " Start Running The Experiment [" + experimentNumber + "] of date [" + d + "] "
					+ "Results will be named as [" + outputFileName + "]");

			status = ExecutionState.RUNNING;

			currentExperiment = new Experiment(experimentNumber, experimentDescription, rspEngine.getName(), inputFileName, outputFileName,
					System.currentTimeMillis(), comment, 0L);

			log.debug("Status [" + status + "] Experiment Created");
			ExecutionState engineStatus = rspEngine.startProcessing();
			log.debug("Status [" + status + "] Processing is started");

			if (ExecutionState.READY.equals(engineStatus)) {
				try {
					RSPEventStreamer.stream(getBuffer(inputFileName), experimentNumber);
				} catch (IOException ex) {
					status = ExecutionState.ERROR;
					log.error(ex.getMessage());
					return 0;
				}
			}

			engineStatus = rspEngine.stopProcessing();
			log.debug("Status [" + status + "] Processing is ended");

			currentExperiment.setTimestampEnd(System.currentTimeMillis());
			experimentResultCollector.store(currentExperiment);

			if (ExecutionState.CLOSED.equals(engineStatus)) {
				status = ExecutionState.READY;
			}

			log.info("Status [" + status + "] Stop the Streamign " + System.currentTimeMillis());

		}
		return 1;
	}

	/**
	 * 
	 * Start the system execution Move the system state from ON to RUNNING
	 * 
	 * @param experimentNumber
	 * 
	 * @throws Exception
	 */
	public int run(String f, int experimentNumber) throws Exception {
		return run(f, experimentNumber, "", new Date());
	}

	@Override
	public ExecutionState close() {
		if (!isOn()) {
			throw new WrongStatusTransitionException("Can't Switch from Status [" + status + "] to [" + ExecutionState.CLOSED + "]");
		} else {
			ExecutionState RSPEventStreamerStatus = RSPEventStreamer.close();
			ExecutionState engineStatus = rspEngine.close();
			ExecutionState collectorStatus = resultCollector.close();
			ExecutionState experimenTcollectorStatus = experimentResultCollector.close();

			if (ExecutionState.CLOSED.equals(RSPEventStreamerStatus) && ExecutionState.CLOSED.equals(experimenTcollectorStatus)
					&& ExecutionState.CLOSED.equals(collectorStatus) && ExecutionState.CLOSED.equals(engineStatus)) {
				status = ExecutionState.CLOSED;
				log.debug("Status [" + status + "] Closing the TestStand");
			} else {
				log.error("RSPEventStreamerStatus: " + RSPEventStreamerStatus);
				log.error("collectorStatus: " + collectorStatus);
				log.error("experimentCollectorStatus: " + experimenTcollectorStatus);
				log.error("engineStatus: " + engineStatus);
				status = ExecutionState.ERROR;
			}
			return status;
		}
	}

	@Override
	public ExecutionState init() {
		if (!isStartable()) {
			throw new WrongStatusTransitionException("Can't Switch from Status [" + status + "] to [" + ExecutionState.READY + "]");
		} else {
			ExecutionState streamerStatus = RSPEventStreamer.init();
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

	public boolean isStartable() {
		return ExecutionState.NOT_READY.equals(status) || ExecutionState.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionState.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionState.READY.equals(status);
	}

	/**
	 * Save the current execution state to log an error Stop the execution
	 * completely
	 * 
	 * @return
	 */
	public ExecutionState stop() {
		return status = ExecutionState.OFF;
	}

	@Override
	public boolean process(RSPEvent e) {
		// TODO call the collector to ensure pull call
		se = e;
		return rspEngine.process(e);
	}

	@Override
	public boolean store(EventResult r) {
		try {
			return resultCollector.store(r);
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean processDone() {
		// TODO Auto-generated method stub
		return false;
	}

}
