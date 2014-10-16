package it.polimi.processing.teststand.core;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.events.result.ExperimentResultEvent;
import it.polimi.processing.events.result.StreamingEventResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.streamer.Streamer;
import it.polimi.processing.teststand.exceptions.WrongStatusTransitionException;
import it.polimi.utils.FileUtils;
import it.polimi.utils.TripleGraphTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import lombok.Getter;

import org.apache.log4j.Logger;

@Getter
public class TestStand<T extends RSPEngine> extends Stand implements
		EventProcessor<StreamingEvent>, ResultCollector<StreamingEventResult>,
		Startable<ExecutionStates> {

	private StartableCollector<StreamingEventResult> resultCollector;
	private StartableCollector<ExperimentResultEvent> experimentResultCollector;

	private T rspEngine;
	private Streamer streamer;

	public TestStand() {
		super(ExecutionStates.NOT_READY, null);
	}

	public void build(
			StartableCollector<StreamingEventResult> resultCollector,
			StartableCollector<ExperimentResultEvent> experimentResultCollector,
			T rspEngine, Streamer streamer) {
		this.experimentResultCollector = experimentResultCollector;
		this.resultCollector = resultCollector;
		this.rspEngine = rspEngine;
		this.streamer = streamer;
	}

	/**
	 * 
	 * Start the system execution Move the system state from ON to RUNNING
	 * 
	 * @throws Exception
	 */
	public void run(String f) throws Exception {
		Logger.getRootLogger().info("START STREAMING "+System.currentTimeMillis());
		String experimentName = "EXPERIMENT_ON_" + f + "_WITH_ENGINE_"
				+ rspEngine.getName();
		String inputFileName = FileUtils.INPUT_FILE_PATH + f;
		String outputFileName = "_Result_" + f.split("\\.")[0];
		String logFileName = "LOG_" + f.split("\\.")[0];

		if (!isOn()) {
			throw new WrongStatusTransitionException("Not ON");
		} else {

			Logger.getLogger("obqa").debug("RUN");
			// Start running
			status = ExecutionStates.RUNNING;

			currentExperiment = new Experiment(rspEngine.getName(),
					experimentName, inputFileName, outputFileName, logFileName);

			ExecutionStates engineStatus = rspEngine.startProcessing();

			if (ExecutionStates.READY.equals(engineStatus)) {

				try {
					streamer.stream(TripleGraphTypes.UTRIPLES,
							rspEngine.getName(), inputFileName,
							getBuffer(inputFileName));

				} catch (IOException ex) {
					status = ExecutionStates.ERROR;
					ex.printStackTrace();
				}
			}

			engineStatus = rspEngine.stopProcessing();

			experimentResultCollector.store(new ExperimentResultEvent(
					currentExperiment));

			if (ExecutionStates.CLOSED.equals(engineStatus)) {
				status = ExecutionStates.READY;
			}

			Logger.getRootLogger().info("STOP STREAMING "+System.currentTimeMillis());

		}

	}

	@Override
	public ExecutionStates close() {
		if (!isOn()) {
			throw new WrongStatusTransitionException(
					"Can't move from a status different from ON Current: "
							+ status);
		} else {
			ExecutionStates streamerStatus = streamer.close();
			ExecutionStates engineStatus = rspEngine.close();
			ExecutionStates collectorStatus = resultCollector.close();
			ExecutionStates experimenTcollectorStatus = experimentResultCollector
					.close();

			if (ExecutionStates.CLOSED.equals(streamerStatus)
					&& ExecutionStates.CLOSED.equals(experimenTcollectorStatus)
					&& ExecutionStates.CLOSED.equals(collectorStatus)
					&& ExecutionStates.CLOSED.equals(engineStatus)) {
				return status = ExecutionStates.CLOSED;
			} else {
				Logger.getLogger("obqa").error(
						"streamerStatus: " + streamerStatus);
				Logger.getLogger("obqa").error(
						"collectorStatus: " + collectorStatus);
				Logger.getLogger("obqa").error(
						"experimentCollectorStatus: "
								+ experimenTcollectorStatus);
				Logger.getLogger("obqa").error("engineStatus: " + engineStatus);
				return status = ExecutionStates.ERROR;
			}

		}
	}

	@Override
	public ExecutionStates init() {
		if (isStartable()) {
			ExecutionStates streamerStatus = streamer.init();
			ExecutionStates engineStatus = rspEngine.init();
			ExecutionStates collectorStatus = resultCollector.init();
			ExecutionStates experimenTcollectorStatus = experimentResultCollector
					.init();

			if (ExecutionStates.READY.equals(streamerStatus)
					&& ExecutionStates.READY.equals(collectorStatus)
					&& ExecutionStates.READY.equals(engineStatus)
					&& ExecutionStates.READY.equals(experimenTcollectorStatus)) {
				return status = ExecutionStates.READY;
			} else {
				Logger.getLogger("obqa").error(
						"streamerStatus: " + streamerStatus);
				Logger.getLogger("obqa").error(
						"collectorStatus: " + collectorStatus);
				Logger.getLogger("obqa").error(
						"experimentCollectorStatus: "
								+ experimenTcollectorStatus);
				Logger.getLogger("obqa").error("engineStatus: " + engineStatus);
				return status = ExecutionStates.ERROR;
			}

		} else {
			throw new WrongStatusTransitionException(
					"Can't move from a status different from OFF Current: "
							+ status);
		}

	}

	public boolean isStartable() {
		return ExecutionStates.NOT_READY.equals(status)
				|| ExecutionStates.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionStates.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionStates.READY.equals(status);
	}

	/**
	 * Save the current execution state to log an error Stop the execution
	 * completely
	 * 
	 * @return
	 */
	public ExecutionStates stop() {
		return status = ExecutionStates.OFF;
	}

	// TODO Unders
	public boolean sendEvent(StreamingEvent e) {
		return rspEngine.sendEvent(e);
	}

	public boolean store(StreamingEventResult r) {
		try {
			return resultCollector.store(r);
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public long getTimestamp() {
		return resultCollector.getTimestamp();
	}

	@Override
	public StreamingEventResult newEventInstance(Set<String[]> all_triples,
			Event e) {
		// TODO folder
		// return new StreamingEventResult(all_triples, start_triples,
		// event_timestamp, stand.getCurrentExperiment()
		// .getOutputFileName(), "plain/", stand
		// .getCurrentExperiment().getName(), stand
		// .getCurrentExperiment().getTimestamp(), lineNumber);

		return new StreamingEventResult(all_triples, (StreamingEvent) e,
				currentExperiment.getOutputFileName());
	}

	private BufferedReader getBuffer(String fileName)
			throws FileNotFoundException {
		File file = new File(fileName);
		return new BufferedReader(new FileReader(file));
	}

}
