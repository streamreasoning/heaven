package it.polimi.comparator.core;

import it.polimi.comparator.engine.EngineComparator;
import it.polimi.comparator.events.ComparisonExperimentResult;
import it.polimi.comparator.events.ComparisonResultEvent;
import it.polimi.enums.ExecutionStates;
import it.polimi.events.Experiment;
import it.polimi.output.filesystem.FileManager;
import it.polimi.output.result.ResultCollector;
import it.polimi.streamer.Streamer;
import it.polimi.teststand.exceptions.WrongStatusTransitionException;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class CalibrationStand<T extends EngineComparator> {

	private T engine;
	private ExecutionStates status;
	private ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> resultCollector;
	private Streamer<T> streamer;
	private String[] comparing_files;
	private Experiment currentExperiment;

	public CalibrationStand(
			String[] comparing_files,
			String[] files,
			ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> resultCollector,
			T engine, Streamer<T> streamer) throws ClassNotFoundException,
			SQLException {
		status = ExecutionStates.NOT_READY;
		this.resultCollector = resultCollector;
		this.streamer = streamer;
		this.engine = engine;
		this.comparing_files = comparing_files;
	}

	public void run(String fileName) {
		if (!isOn()) {
			throw new WrongStatusTransitionException("Not ON");
		} else {
			String comparing_fileName = "";
			for (String s : comparing_files) {
				comparing_fileName += "_" + s;

			}
			currentExperiment = new Experiment(
					engine.getName(),
					FileManager.DATA_FILE_PATH + "output/" + comparing_fileName,
					FileManager.DATA_FILE_PATH + "output_comparator/"
							+ engine.getName() + "/_Result_"
							+ comparing_fileName + ".txt");

			ExecutionStates engineStatus = engine
					.startProcessing(currentExperiment);
			if (ExecutionStates.READY.equals(engineStatus)) {
				try {
					streamer.stream(FileManager
							.getBuffer(FileManager.DATA_FILE_PATH + "input/"
									+ fileName));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			engineStatus = engine.stopProcessing(currentExperiment);

			if (ExecutionStates.OFF.equals(engineStatus)) {
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
			ExecutionStates collectorStatus = resultCollector.close();
			ExecutionStates engineStatus = engine.close();

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
		if (isOFF()) {
			ExecutionStates streamerStatus = streamer.init();
			ExecutionStates engineStatus = engine.init();
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

	public boolean isOFF() {
		return ExecutionStates.NOT_READY.equals(status)
				|| ExecutionStates.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionStates.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionStates.READY.equals(status);
	}
}
