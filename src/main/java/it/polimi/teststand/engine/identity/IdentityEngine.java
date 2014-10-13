package it.polimi.teststand.engine.identity;

import it.polimi.enums.ExecutionStates;
import it.polimi.events.Experiment;
import it.polimi.events.StreamingEvent;
import it.polimi.events.result.StreamingEventResult;
import it.polimi.teststand.core.TestStand;
import it.polimi.teststand.engine.RSPEngine;

import java.io.IOException;

import org.apache.log4j.Logger;

public class IdentityEngine extends RSPEngine {

	private Experiment currentExperiment;

	public IdentityEngine(TestStand<RSPEngine> stand) {
		super(stand);
		currentExperiment = stand.getCurrentExperiment();
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		try {
			if (currentExperiment != null) {
				return collector.store(new StreamingEventResult(
						e.getEventTriples(), e, currentExperiment
								.getOutputFileName()));
			} else {
				Logger.getRootLogger().debug(
						"An Experiment must be initialized");
				return false;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

	}

	@Override
	public ExecutionStates startProcessing() {
		if (currentExperiment != null) {
			return status = ExecutionStates.READY;
		} else
			return status = ExecutionStates.ERROR;
	}

	@Override
	public ExecutionStates stopProcessing() {
		if (isOn()) {
			return status = ExecutionStates.CLOSED;
		} else
			return status = ExecutionStates.ERROR;
	}

	@Override
	public ExecutionStates init() {
		Logger.getRootLogger().info("Nothing to do...Turing Off");
		return status = ExecutionStates.READY;
	}

	@Override
	public ExecutionStates close() {
		Logger.getRootLogger().info("Nothing to do...Turing Off");
		return status = ExecutionStates.CLOSED;
	}

	public boolean isStartable() {
		return ExecutionStates.READY.equals(status)
				|| ExecutionStates.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionStates.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionStates.READY.equals(status);
	}

}