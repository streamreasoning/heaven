package it.polimi.comparator.core;

import it.polimi.comparator.engine.EngineComparator;
import it.polimi.comparator.events.ComparisonExperimentResult;
import it.polimi.comparator.events.ComparisonResultEvent;
import it.polimi.comparator.output.ResultCollectorComparator;
import it.polimi.events.Experiment;
import it.polimi.output.filesystem.FileManager;
import it.polimi.output.filesystem.FileManagerImpl;
import it.polimi.output.result.ResultCollector;
import it.polimi.output.sqllite.DatabaseManagerImpl;
import it.polimi.streamer.Streamer;
import it.polimi.teststand.enums.ExecutionStates;
import it.polimi.teststand.exceptions.WrongStatusTransitionException;

import java.io.IOException;
import java.sql.SQLException;

public class CalibrationStand<T extends EngineComparator> {

	private ExecutionStates status = ExecutionStates.OFF;
	private ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> resultCollector;
	private T engine;
	private Streamer<T> streamer;
	private String[] files, comparing_files;

	public CalibrationStand(String[] comparing_files, String[] files, T engine)
			throws ClassNotFoundException, SQLException {
		this.comparing_files = comparing_files;
		this.files = files;
		resultCollector = new ResultCollectorComparator(new FileManagerImpl(),
				new DatabaseManagerImpl());
		this.engine = engine;
		engine.setResultCollector(resultCollector);
		streamer = new Streamer<T>(engine);
	}

	public void run() {
		if (!isOn()) {
			throw new WrongStatusTransitionException("Not ON");
		} else {
			String comparing_fileName = "";
			for (String s : comparing_files) {
				comparing_fileName += "_" + s;

			}
			if (engine
					.startProcessing(new Experiment(engine.getName(),
							FileManager.DATA_FILE_PATH + "output/"
									+ comparing_fileName,
							FileManager.DATA_FILE_PATH + "output_comparator/"
									+ engine.getName() + "/_Result_"
									+ comparing_fileName + ".txt"))) {
				status = ExecutionStates.READY;
			}
			
			for (String fileName : files) {
				try {
					streamer.stream(
							status,
							FileManager.getBuffer(FileManager.DATA_FILE_PATH
									+ "input/" + fileName));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			engine.stopProcessing();
			status = ExecutionStates.ON;
		}

	}

	public ExecutionStates turnOff() {
		if (isOn()) {
			resultCollector.stop();
			engine.turnOff();
			this.status = ExecutionStates.OFF;
			return status;
		} else {
			throw new WrongStatusTransitionException(
					"Can't move from a status different from ON Current: "
							+ status);
		}
	}

	public ExecutionStates turnOn() {
		if (isOFF()) {
			resultCollector.start();
			engine.turnOn();
			this.status = ExecutionStates.ON;
			return status;
		} else {
			throw new WrongStatusTransitionException(
					"Can't move from a status different from OFF Current: "
							+ status);
		}

	}

	public boolean isOFF() {
		return ExecutionStates.OFF.equals(status);
	}

	public boolean isOn() {
		return ExecutionStates.ON.equals(status);
	}

	public boolean isReady() {
		return ExecutionStates.READY.equals(status);
	}
}
