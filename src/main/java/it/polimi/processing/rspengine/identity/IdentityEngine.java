package it.polimi.processing.rspengine.identity;

import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.events.result.StreamingEventResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.teststand.core.TestStand;

import java.io.IOException;

import lombok.extern.log4j.Log4j;

@Log4j
public class IdentityEngine extends RSPEngine {

	private final Experiment currentExperiment;

	public IdentityEngine(String name, TestStand<RSPEngine> stand) {
		super(name, stand);
		currentExperiment = stand.getCurrentExperiment();
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		try {
			if (currentExperiment != null) {
				return collector.store(new StreamingEventResult(e, e.getEventTriples(), System.currentTimeMillis()),
						name + "/" + currentExperiment.getOutputFileName());
			}
			log.debug("An Experiment must be initialized");
		} catch (IOException e1) {
			log.error(e1.getMessage());
		}
		return false;

	}

	@Override
	public ExecutionStates startProcessing() {
		if (currentExperiment != null) {
			status = ExecutionStates.READY;
		} else {
			log.warn("The Experiment is Null");
			status = ExecutionStates.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionStates stopProcessing() {
		if (isOn()) {
			status = ExecutionStates.CLOSED;
		} else {
			status = ExecutionStates.ERROR;
		}
		return status;
	}

	@Override
	public ExecutionStates init() {
		log.info("Nothing to do...Turing Off");
		status = ExecutionStates.READY;
		return status;
	}

	@Override
	public ExecutionStates close() {
		log.info("Nothing to do...Turing Off");
		status = ExecutionStates.CLOSED;
		return status;
	}

	public boolean isStartable() {
		return ExecutionStates.READY.equals(status) || ExecutionStates.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionStates.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionStates.READY.equals(status);
	}

}
