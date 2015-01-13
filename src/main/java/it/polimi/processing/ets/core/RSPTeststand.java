package it.polimi.processing.ets.core;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.ets.collector.TSResultCollector;
import it.polimi.processing.ets.streamer.TSStreamer;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.InputRDFStream;
import it.polimi.processing.events.results.OutputRDFStream;
import it.polimi.processing.events.results.TSResult;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.services.system.GetPropertyValues;
import it.polimi.services.system.Memory;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@NoArgsConstructor
public class RSPTeststand extends TestStand {

	private int experimentNumber;
	private String outputFileName, windowFileName;
	private String where;

	@Override
	public boolean process(InputRDFStream e) {
		totalEvent++;
		return (e instanceof OutputRDFStream) ? process((OutputRDFStream) e) : processRSPTripleSet(e);
	}

	public boolean processRSPTripleSet(InputRDFStream e) {
		this.rspEvent++;
		this.eventNumber = engine.getEventNumber();

		this.currentResult = new TSResult();
		this.currentResult.setId("<http://example.org/" + experimentNumber + "/" + eventNumber + ">");
		this.currentResult.setEventNumber(eventNumber);
		this.currentResult.setMemoryB(Memory.getMemoryUsage());
		this.currentResult.setInputTimestamp(System.currentTimeMillis());

		boolean process = engine.process(e);
		engine.timeProgress();
		return process;
	}

	public boolean process(OutputRDFStream engineResult) {
		double memoryA = Memory.getMemoryUsage();
		this.where = "exp" + experimentNumber + "/" + engine.getName() + "/";
		if (engineResult.isAbox()) {
			this.where += windowFileName;
			return processAbox(engineResult);

		} else {
			resultEvent++;
			this.where += outputFileName;
			this.currentResult.setResult(engineResult);
			this.currentResult.setMemoryA(memoryA);
			// this.currentResult.setCr(engineResult.getCompleteRHODF());
			// this.currentResult.setSr(engineResult.getSoundRHODF());
			// this.currentResult.setCs(engineResult.getCompleteSMPL());
			// this.currentResult.setSs(engineResult.getSoundSMPL());
			boolean ret = collector.process(currentResult, this.where);
			this.tsResultEvents += ret ? 1 : 0;
			return ret;
		}

	}

	private boolean processAbox(OutputRDFStream engineResult) {

		this.aboxResult = new TSResult();
		this.aboxResult.setId("<http://example.org/" + experimentNumber + "/" + (engine.getEventNumber() - 1) + ">");
		this.aboxResult.setEventNumber(engineResult.getEventNumber());
		this.aboxResult.setInputTimestamp(engineResult.getInputTimestamp());
		this.aboxResult.setResult(engineResult);

		return collector.process(aboxResult, this.where);
	}

	@Override
	public void build(TSStreamer rspEventStreamer, RSPEngine rspEngine, TSResultCollector resultCollector) {
		super.build(rspEventStreamer, rspEngine, resultCollector);
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

			ExecutionState engineStatus = engine.startProcessing();

			log.debug("Status [" + status + "] Processing is started");

			if (ExecutionState.READY.equals(engineStatus)) {
				streamer.process(e);
			} else {
				String msg = "Can't start streaming on status [" + status + "]";
				status = ExecutionState.ERROR;
				throw new WrongStatusTransitionException(msg);
			}

			engineStatus = engine.stopProcessing();

			log.debug("Status [" + status + "] Processing is ended");

			currentExperiment.setTimestampEnd(System.currentTimeMillis());

			collector.process(currentExperiment);

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
