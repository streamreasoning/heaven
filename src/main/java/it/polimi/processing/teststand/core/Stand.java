package it.polimi.processing.teststand.core;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Stand {
	protected ExecutionState status = ExecutionState.OFF;
	protected Experiment currentExperiment;

	public BufferedReader getBuffer(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		return new BufferedReader(new FileReader(file));
	}

	protected boolean isStartable() {
		return ExecutionState.NOT_READY.equals(status) || ExecutionState.CLOSED.equals(status);
	}

	protected boolean isOn() {
		return ExecutionState.READY.equals(status);
	}

	protected boolean isReady() {
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
}
