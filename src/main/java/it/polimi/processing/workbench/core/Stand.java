package it.polimi.processing.workbench.core;

import it.polimi.processing.enums.ExecutionState;
import lombok.Getter;

@Getter
public abstract class Stand {

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

	/**
	 * Save the current execution state to log an error Stop the execution
	 * completely
	 * 
	 * @return
	 */
	public ExecutionState stop() {
		status = ExecutionState.OFF;
		return status;
	}
}
