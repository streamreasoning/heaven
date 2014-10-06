package it.polimi.comparator;

import it.polimi.output.filesystem.FileManager;
import it.polimi.output.filesystem.FileManagerImpl;
import it.polimi.output.result.ResultCollector;
import it.polimi.output.result.ResultCollectorImpl;
import it.polimi.output.sqllite.DatabaseManagerImpl;
import it.polimi.streamer.Streamer;
import it.polimi.teststand.enums.ExecutionStates;
import it.polimi.teststand.events.Experiment;
import it.polimi.teststand.exceptions.WrongStatusTransitionException;

import java.io.IOException;
import java.sql.SQLException;

public class CalibrationStand<T extends EngineComparator> {

	private ExecutionStates status = ExecutionStates.OFF;
	private ResultCollector resultCollector;
	private T rspEngine;
	private Streamer<T> streamer;
	private String[] files, comparing_files;

	public CalibrationStand(String[] comparing_files, String[] files, T engine)
			throws ClassNotFoundException, SQLException {
		this.comparing_files = comparing_files;
		this.files = files;

		resultCollector = new ResultCollectorImpl(new FileManagerImpl(),
				new DatabaseManagerImpl());
		this.rspEngine = engine;
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
			if (rspEngine
					.startProcessing(new Experiment(rspEngine.getName(),
							FileManager.DATA_FILE_PATH + "output/"
									+ comparing_fileName,
							FileManager.DATA_FILE_PATH
									+ "output_comparator/"
									+ rspEngine.getName()
									+ "/_Result_"
									+ comparing_fileName+".txt"))) {
				status = ExecutionStates.READY;
			}
			for (String fileName : files) {
				try {
					/*
					 * TODO metodo statico per ottenre il bugger di lettura, ho
					 * preferito lasciare tutto l'accesso al filesystem nella
					 * stessa classe
					 */
					streamer.stream(
							status,
							FileManager.getBuffer(FileManager.DATA_FILE_PATH
									+ "input/" + fileName));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			rspEngine.stopProcessing();
			status = ExecutionStates.ON;
		}

	}

	public ExecutionStates turnOff() {
		if (isOn()) {
			resultCollector.stop();
			rspEngine.turnOff();
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
			rspEngine.turnOn();
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
