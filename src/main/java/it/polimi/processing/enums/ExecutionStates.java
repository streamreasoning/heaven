package it.polimi.processing.enums;

public enum ExecutionStates {

	/**
	 * OFF: Nothing can be done, used by only the Envirorments (Stands)
	 * NOT_READY: some initialization is required READY: processing can start
	 * RUNNING: processing is currently active CLOSED: as OFF but can be
	 * restarted ERROR: something went wrong
	 * 
	 */
	NOT_READY, READY, RUNNING, CLOSED, ERROR, OFF;
}
