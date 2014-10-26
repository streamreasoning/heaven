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
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.teststand.streamer.NTStreamer;
import it.polimi.utils.FileUtils;
import it.polimi.utils.TripleGraphTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

import lombok.Getter;

import org.apache.http.impl.cookie.DateUtils;
import org.apache.log4j.Logger;

@Getter
public class TestStand<T extends RSPEngine> extends Stand implements
		EventProcessor<StreamingEvent>, ResultCollector<StreamingEventResult>,
		Startable<ExecutionStates> {

	private StartableCollector<StreamingEventResult> resultCollector;
	private StartableCollector<ExperimentResultEvent> experimentResultCollector;

	private T rspEngine;
	private NTStreamer<StreamingEvent> streamer;

	public TestStand() {
		super(ExecutionStates.NOT_READY, null);
	}

	public void build(
			StartableCollector<StreamingEventResult> resultCollector,
			StartableCollector<ExperimentResultEvent> experimentResultCollector,
			T rspEngine, NTStreamer<StreamingEvent> streamer) {
		this.experimentResultCollector = experimentResultCollector;
		this.resultCollector = resultCollector;
		this.rspEngine = rspEngine;
		this.streamer = streamer;
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
		Logger.getRootLogger().info(
				"START STREAMING " + System.currentTimeMillis());
		String experimentDescription = "EXPERIMENT_ON_" + f + "_WITH_ENGINE_"
				+ rspEngine.getName();
		String inputFileName = FileUtils.INPUT_FILE_PATH + f;
		Date d = new Date();
		String outputFileName = "Result_"
				+ DateUtils.formatDate(d, "YYYY_MM_dd_HH_mm_SS") + "_"
				+ f.split("\\.")[0];

		if (!isOn()) {
			throw new WrongStatusTransitionException("Not ON");
		} else {

			Logger.getLogger("obqa").debug("RUN");
			// Start running
			status = ExecutionStates.RUNNING;

			currentExperiment = new Experiment(experimentNumber,
					experimentDescription, rspEngine.getName(), inputFileName,
					outputFileName, System.currentTimeMillis());

			ExecutionStates engineStatus = rspEngine.startProcessing();

			if (ExecutionStates.READY.equals(engineStatus)) {

				try {
					streamer.stream(getBuffer(inputFileName), experimentNumber,
							rspEngine.getName(), TripleGraphTypes.UTRIPLES);

				} catch (IOException ex) {
					status = ExecutionStates.ERROR;
					ex.printStackTrace();
					return 0;
				}
			}

			engineStatus = rspEngine.stopProcessing();

			String where = "";
			experimentResultCollector.store(new ExperimentResultEvent(
					currentExperiment, System.currentTimeMillis()), where);

			if (ExecutionStates.CLOSED.equals(engineStatus)) {
				status = ExecutionStates.READY;
			}

			Logger.getRootLogger().info(
					"STOP STREAMING " + System.currentTimeMillis());

		}
		return 1;

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
	@Override
	public boolean sendEvent(StreamingEvent e) {
		return rspEngine.sendEvent(e);
	}

	@Override
	public boolean store(StreamingEventResult r, String where) {
		try {
			return resultCollector.store(r, rspEngine.getName() + "/"
					+ currentExperiment.getOutputFileName());
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
		return new StreamingEventResult((StreamingEvent) e, all_triples,
				System.currentTimeMillis());
	}

	private BufferedReader getBuffer(String fileName)
			throws FileNotFoundException {
		File file = new File(fileName);
		return new BufferedReader(new FileReader(file));
	}

}
