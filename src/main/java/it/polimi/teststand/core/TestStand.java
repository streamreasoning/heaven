package it.polimi.teststand.core;

import it.polimi.output.filesystem.FileManager;
import it.polimi.output.filesystem.FileManagerImpl;
import it.polimi.output.result.ResultCollector;
import it.polimi.output.result.ResultCollectorTestStandImpl;
import it.polimi.output.sqllite.DatabaseManagerImpl;
import it.polimi.streamer.Streamer;
import it.polimi.teststand.engine.RSPEngine;
import it.polimi.teststand.enums.ExecutionStates;
import it.polimi.teststand.events.Experiment;
import it.polimi.teststand.events.TestExperimentResultEvent;
import it.polimi.teststand.events.TestResultEvent;
import it.polimi.teststand.exceptions.WrongStatusTransitionException;

import java.io.IOException;
import java.sql.SQLException;

public class TestStand<T extends RSPEngine> {

	private ExecutionStates status = ExecutionStates.OFF;
	private ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector;
	private T rspEngine;
	private Streamer<RSPEngine> streamer;
	private String[] files;

	public TestStand(String[] files, T rspEngine)
			throws ClassNotFoundException, SQLException {
		this.files = files;
		resultCollector = new ResultCollectorTestStandImpl(new FileManagerImpl(),
				new DatabaseManagerImpl());
		this.rspEngine = rspEngine;
		rspEngine.setResultCollector(resultCollector);
		streamer = new Streamer<RSPEngine>(rspEngine);
	}

	public void run() {
		if (!isOn()) {
			throw new WrongStatusTransitionException("Not ON");
		} else {

			for (String fileName : files) {
				if (rspEngine
						.startProcessing(new Experiment(rspEngine.getName(),
								FileManager.DATA_FILE_PATH + "input/"
										+ fileName, FileManager.DATA_FILE_PATH
										+ "output/"
										+ rspEngine.getName()
										+ "/_Result_"
										+ fileName.substring(0,
												fileName.length() - 3) + "trig"))) {
					status = ExecutionStates.READY;
				}

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
				rspEngine.stopProcessing();
				status = ExecutionStates.ON;

			}
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
