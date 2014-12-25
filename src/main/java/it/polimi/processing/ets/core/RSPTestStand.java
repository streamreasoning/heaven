package it.polimi.processing.ets.core;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.ets.timecontrol.TimeStrategy;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.Result;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.services.FileService;
import it.polimi.processing.streamer.RSPTripleSetStreamer;
import it.polimi.processing.system.GetPropertyValues;
import it.polimi.utils.FileUtils;

import java.io.BufferedReader;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Getter
@Setter
@Log4j
public class RSPTestStand extends TestStandImpl {

	private int experimentNumber;
	private String outputFileName, windowFileName, inputFileNameWithPath, experimentDescription;
	private String where;

	private final TimeStrategy timeStrategy;

	public RSPTestStand(TimeStrategy strategy) {
		this.timeStrategy = strategy;
	}

	@Override
	public int run(String f, int experimentNumber, String comment, String outputFileName, String windowFileName, String experimentDescription) {
		this.experimentNumber = experimentNumber;
		this.experimentDescription = experimentDescription;
		this.inputFileNameWithPath = FileUtils.INPUT_FILE_PATH + f;
		this.outputFileName = outputFileName;
		this.windowFileName = windowFileName;

		long startTime = System.currentTimeMillis();

		if (!isOn()) {
			throw new WrongStatusTransitionException("Can't run in Status [" + status + "]");
		} else {
			log.info("Status [" + status + "]" + " Start Running The Experiment [" + experimentNumber + "] of date ["
					+ GetPropertyValues.getDateProperty("experiment_date") + "] " + "Results will be named as [" + outputFileName + "]");

			status = ExecutionState.RUNNING;
			currentExperiment = new Experiment(experimentNumber, experimentDescription, rspEngine.getName(), inputFileNameWithPath, outputFileName,
					startTime, comment, 0L);

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

			experimentResultCollector.process(currentExperiment);

			if (ExecutionState.CLOSED.equals(engineStatus)) {
				status = ExecutionState.READY;
			} else if (ExecutionState.ERROR.equals(engineStatus)) {
				status = ExecutionState.ERROR;
			}

			log.info("Status [" + status + "] Stop the experiment, duration " + (System.currentTimeMillis() - startTime) + "ms");

		}

		return 1;
	}

	@Override
	public boolean process(Event e) {
		totalEvent++;
		return (e instanceof RSPTripleSet) ? process((RSPTripleSet) e) : process((Result) e);
	}

	public boolean process(RSPTripleSet e) {
		rspEvent++;
		return timeStrategy.apply(e);
	}

	public boolean process(Result engineResult) {
		this.where = "exp" + experimentNumber + "/" + rspEngine.getName() + "/";

		if (engineResult.isAbox()) {
			this.where += windowFileName;
			log.info(this.where);
			engineResult.setId("<http://example.org/" + experimentNumber + "/" + (rspEngine.getEventNumber() - 1) + ">");
			resultCollector.process(engineResult, this.where);
			return true;
		} else {
			resultEvent++;
			this.where += outputFileName;
			this.currentResult = timeStrategy.getResult();
			this.currentResult.setStatements(engineResult.getStatements());
			this.currentResult.setMemoryA(engineResult.getMemory());
			this.currentResult.setOutputTimestamp(engineResult.getTimestamp());
			this.currentResult.setCr(engineResult.getCompleteRHODF());
			this.currentResult.setSr(engineResult.getSoundRHODF());
			this.currentResult.setCs(engineResult.getCompleteSMPL());
			this.currentResult.setSs(engineResult.getSoundSMPL());
			return processDone();
		}

	}

	@Override
	public boolean processDone() {
		boolean ret = resultCollector.process(currentResult, this.where);
		this.tsResultEvents += ret ? 1 : 0;
		return ret;
	}

	@Override
	public void build(StartableCollector<EventResult> resultCollector, StartableCollector<ExperimentResult> experimentResultCollector,
			RSPEngine rspEngine, RSPTripleSetStreamer rspEventStreamer) {
		this.timeStrategy.setRSPEngine(rspEngine);
		super.build(resultCollector, experimentResultCollector, rspEngine, rspEventStreamer);
	}
}
