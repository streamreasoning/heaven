package it.polimi.processing.ets.core;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.ets.timecontrol.TimeStrategy;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.results.EventResult;
import it.polimi.processing.events.results.Result;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.streamer.RSPTripleSetStreamer;
import it.polimi.services.FileService;
import it.polimi.services.system.GetPropertyValues;
import it.polimi.services.system.Memory;

import java.io.BufferedReader;

import lombok.extern.log4j.Log4j;

@Log4j
public class RSPTestStand extends TestStandImpl {

	private int experimentNumber;
	private String outputFileName, windowFileName, inputFileNameWithPath;
	private String where;

	private final TimeStrategy timeStrategy;

	public RSPTestStand(TimeStrategy strategy) {
		this.timeStrategy = strategy;
	}

	@Override
	public boolean process(Event e) {
		totalEvent++;
		return (e instanceof Result) ? process((Result) e) : process((RSPTripleSet) e);
	}

	public boolean process(RSPTripleSet e) {
		rspEvent++;
		return timeStrategy.apply(e);
	}

	public boolean process(Result engineResult) {
		double memoryA = Memory.getMemoryUsage();
		this.where = "exp" + experimentNumber + "/" + rspEngine.getName() + "/";

		if (engineResult.isAbox()) {
			this.where += windowFileName;
			return processAbox(engineResult);

		} else {
			resultEvent++;
			this.where += outputFileName;
			this.currentResult = timeStrategy.getResult();
			this.currentResult.setResult(engineResult);
			this.currentResult.setMemoryA(memoryA);
			// this.currentResult.setCr(engineResult.getCompleteRHODF());
			// this.currentResult.setSr(engineResult.getSoundRHODF());
			// this.currentResult.setCs(engineResult.getCompleteSMPL());
			// this.currentResult.setSs(engineResult.getSoundSMPL());
			return processDone();
		}

	}

	private boolean processAbox(Result engineResult) {
		engineResult.setId("<http://example.org/" + experimentNumber + "/" + (rspEngine.getEventNumber() - 1) + ">");
		resultCollector.process(engineResult, this.where);
		return true;
	}

	@Override
	public boolean processDone() {
		boolean ret = resultCollector.process(currentResult, this.where);
		this.tsResultEvents += ret ? 1 : 0;
		return ret;
	}

	@Override
	public void build(ResultCollector<EventResult> resultCollector, RSPEngine rspEngine, RSPTripleSetStreamer rspEventStreamer) {
		this.timeStrategy.setRSPEngine(rspEngine);
		super.build(resultCollector, rspEngine, rspEventStreamer);
	}

	@Override
	public int run(Experiment e) {
		return run(e, "");
	}

	@Override
	public int run(Experiment e, String comment) {

		if (!isOn()) {
			throw new WrongStatusTransitionException("Can't run in Status [" + status + "]");
		} else if (e != null) {
			this.experimentNumber = e.getExperimentNumber();
			this.inputFileNameWithPath = e.getInputFileName();
			this.outputFileName = e.getOutputFileName();
			this.windowFileName = e.getWindowFileName();

			long startTime = System.currentTimeMillis();

			log.info("Status [" + status + "]" + " Start Running The Experiment [" + experimentNumber + "] of date ["
					+ GetPropertyValues.getDateProperty("experiment_date") + "] " + "Results will be named as [" + outputFileName + "]");

			status = ExecutionState.RUNNING;

			currentExperiment = e;
			e.setComment(comment);
			e.setTimestampStart(startTime);

			log.debug("Status [" + status + "] Experiment Created");

			ExecutionState engineStatus = rspEngine.startProcessing();

			log.debug("Status [" + status + "] Processing is started");

			BufferedReader buffer = FileService.getBuffer(inputFileNameWithPath);

			if (ExecutionState.READY.equals(engineStatus)) {
				if (buffer != null) {
					rspEventStreamer.startStreamimng(buffer, experimentNumber);
				} else {
					log.error("Status [" + status + "] Can't start streaming processing");
					status = ExecutionState.ERROR;
					return 0;
				}
			} else {
				String msg = "Can't start streaming on status [" + status + "]";
				status = ExecutionState.ERROR;
				throw new WrongStatusTransitionException(msg);
			}

			engineStatus = rspEngine.stopProcessing();

			log.debug("Status [" + status + "] Processing is ended");

			currentExperiment.setTimestampEnd(startTime);

			resultCollector.process(currentExperiment);

			if (ExecutionState.CLOSED.equals(engineStatus)) {
				status = ExecutionState.READY;
			} else if (ExecutionState.ERROR.equals(engineStatus)) {
				status = ExecutionState.ERROR;
			}

			log.info("Status [" + status + "] Stop the experiment, duration " + (System.currentTimeMillis() - startTime) + "ms");
			return 1;

		}

		return 0;
	}
}
