package it.polimi.teststand.core;

import it.polimi.enums.ExecutionStates;
import it.polimi.events.Experiment;
import it.polimi.output.filesystem.FileManager;
import it.polimi.output.result.ResultCollector;
import it.polimi.streamer.Streamer;
import it.polimi.teststand.engine.RSPEngine;
import it.polimi.teststand.events.TestExperimentResultEvent;
import it.polimi.teststand.events.TestResultEvent;
import it.polimi.teststand.exceptions.WrongStatusTransitionException;

import java.io.IOException;

import org.apache.log4j.Logger;

public class TestStand {

	private ExecutionStates status;
	private ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector;
	private RSPEngine rspEngine;
	private Streamer<RSPEngine> streamer;
	private Experiment currentExperiment;

	public TestStand(
			ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector,
			RSPEngine rspEngine, Streamer<RSPEngine> streamer) {
		this.status = ExecutionStates.NOT_READY;
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
	public void run(String fileName) throws Exception {
		if (!isOn()) {
			throw new WrongStatusTransitionException("Not ON");
		} else {

			Logger.getLogger("obqa").debug("RUN");
			// Start running
			status = ExecutionStates.RUNNING;

			// New Experiment
			String experimentName = "EXPERIMENT_ON_" + fileName
					+ "_WITH_ENGINE_" + rspEngine.getName();

			String inputFileName = FileManager.DATA_FILE_PATH + "input/"
					+ fileName;

			String outputFileName = FileManager.DATA_FILE_PATH + "output/"
					+ rspEngine.getName() + "/_Result_"
					+ fileName.substring(0, fileName.length() - 3) + "trig";

			currentExperiment = new Experiment(experimentName, inputFileName,
					outputFileName);

			// Stream experiment e

			ExecutionStates engineStatus = rspEngine
					.startProcessing(currentExperiment);

			if (ExecutionStates.READY.equals(engineStatus)) {

				try {
					// Stream Data Events
					streamer.stream(FileManager.getBuffer(inputFileName));

				} catch (IOException ex) {
					// TODO handle errors
					status = ExecutionStates.ERROR;
					ex.printStackTrace();
				}
			}

			engineStatus = rspEngine.stopProcessing(currentExperiment);

			if (ExecutionStates.CLOSED.equals(engineStatus)) {
				status = ExecutionStates.READY;
			}

		}

	}

	public ExecutionStates turnOff() {
		if (!isOn()) {
			throw new WrongStatusTransitionException(
					"Can't move from a status different from ON Current: "
							+ status);
		} else {
			ExecutionStates streamerStatus = streamer.close();
			ExecutionStates engineStatus = rspEngine.close();
			ExecutionStates collectorStatus = resultCollector.close();

			if (ExecutionStates.CLOSED.equals(streamerStatus)
					&& ExecutionStates.CLOSED.equals(collectorStatus)
					&& ExecutionStates.CLOSED.equals(engineStatus)) {
				return status = ExecutionStates.CLOSED;
			} else {
				Logger.getLogger("obqa").error(
						"streamerStatus: " + streamerStatus);
				Logger.getLogger("obqa").error(
						"collectorStatus: " + collectorStatus);
				Logger.getLogger("obqa").error("engineStatus: " + engineStatus);
				return status = ExecutionStates.ERROR;
			}

		}
	}

	public ExecutionStates turnOn() {
		if (isStartable()) {
			ExecutionStates streamerStatus = streamer.init();
			ExecutionStates engineStatus = rspEngine.init();
			ExecutionStates collectorStatus = resultCollector.init();
			if (ExecutionStates.READY.equals(streamerStatus)
					&& ExecutionStates.READY.equals(collectorStatus)
					&& ExecutionStates.READY.equals(engineStatus)) {
				return status = ExecutionStates.READY;
			} else {
				Logger.getLogger("obqa").error(
						"streamerStatus: " + streamerStatus);
				Logger.getLogger("obqa").error(
						"collectorStatus: " + collectorStatus);
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
}
