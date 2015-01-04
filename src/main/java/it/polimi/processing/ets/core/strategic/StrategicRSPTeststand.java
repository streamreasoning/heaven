package it.polimi.processing.ets.core.strategic;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.ets.collector.TSResultCollector;
import it.polimi.processing.ets.core.TestStand;
import it.polimi.processing.ets.core.strategic.timecontrol.TimeStrategy;
import it.polimi.processing.ets.streamer.TSStreamer;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.events.results.RSPTripleSetResult;
import it.polimi.processing.events.results.TSResult;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.services.system.GetPropertyValues;
import it.polimi.services.system.Memory;
import lombok.extern.log4j.Log4j;

@Log4j
public class StrategicRSPTeststand extends TestStand {

	private int experimentNumber;
	private String outputFileName, windowFileName;
	private String where;

	private final TimeStrategy timeStrategy;

	public StrategicRSPTeststand(TimeStrategy strategy) {
		this.timeStrategy = strategy;
	}

	@Override
	public boolean process(Event e) {
		totalEvent++;
		return (e instanceof RSPTripleSetResult) ? process((RSPTripleSetResult) e) : process((RSPTripleSet) e);
	}

	public boolean process(RSPTripleSet e) {
		rspEvent++;
		return timeStrategy.apply(e);
	}

	public boolean process(RSPTripleSetResult engineResult) {
		double memoryA = Memory.getMemoryUsage();
		this.where = "exp" + experimentNumber + "/" + engine.getName() + "/";

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

	private boolean processAbox(RSPTripleSetResult engineResult) {
		this.aboxResult = new TSResult();
		this.aboxResult.setId("<http://example.org/" + experimentNumber + "/" + (engine.getEventNumber() - 1) + ">");
		this.aboxResult.setEventNumber(engineResult.getEventNumber());
		this.aboxResult.setInputTimestamp(engineResult.getInputTimestamp());
		this.aboxResult.setResult(engineResult);
		return collector.process(aboxResult, this.where);
	}

	@Override
	public boolean processDone() {
		boolean ret = collector.process(currentResult, this.where);
		this.tsResultEvents += ret ? 1 : 0;
		return ret;
	}

	@Override
	public void build(TSResultCollector resultCollector, RSPEngine rspEngine, TSStreamer rspEventStreamer) {
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
