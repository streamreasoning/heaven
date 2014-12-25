package it.polimi.processing.ets.core;

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

	public ExecutionState stop() {
		status = ExecutionState.CLOSED;
		return status;

	}
}
