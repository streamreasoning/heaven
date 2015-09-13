package it.polimi.heaven.core.tsimpl;

import it.polimi.heaven.core.enums.ExecutionState;
import it.polimi.heaven.core.exceptions.WrongStatusTransitionException;
import it.polimi.heaven.core.ts.TestStand;
import it.polimi.heaven.core.ts.events.Experiment;
import it.polimi.heaven.core.ts.events.HeavenResult;
import it.polimi.heaven.core.ts.events.RSPEngineResult;
import it.polimi.heaven.core.ts.events.Stimulus;
import it.polimi.heaven.core.ts.rspengine.RSPEngine;
import it.polimi.heaven.core.tsimpl.collector.TSResultCollector;
import it.polimi.heaven.core.tsimpl.streamer.TSStreamer;
import it.polimi.heaven.services.system.Memory;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@NoArgsConstructor
public class TestStandImpl extends TestStand {

	private String outputPath = "";
	private String experimentnName = "";
	private String engineName;

	@Override
	public boolean process(Stimulus e) {
		totalEvent++;
		return (e instanceof RSPEngineResult) ? process((RSPEngineResult) e) : processRSPTripleSet(e);
	}

	public boolean processRSPTripleSet(Stimulus e) {
		this.rspEvent++;
		this.eventNumber = engine.getEventNumber();

		this.currentResult = new HeavenResult();
		this.currentResult.setId("<http://example.org/" + currentExperiment.getExperimentNumber() + "/" + eventNumber + ">");
		this.currentResult.setEventNumber(eventNumber);
		this.currentResult.setMemoryB((currentExperiment.isMemoryLog()) ? Memory.getMemoryUsage() : 0D);
		this.currentResult.setInputTimestamp(e.getInputTimestamp());
		this.currentResult.setAboxLog(currentExperiment.isAboxLog());
		this.currentResult.setLatencyLog(currentExperiment.isLatencyLog());
		this.currentResult.setMemoryLog(currentExperiment.isMemoryLog());
		this.currentResult.setResultLog(currentExperiment.isResultLog());
		boolean process = engine.process(e);
		engine.timeProgress();
		return process;
	}

	public boolean process(RSPEngineResult engineResult) {

		if (engineResult.isAbox()) {
			return processAbox(engineResult);
		} else {
			resultEvent++;
			this.currentResult.setResult(engineResult);

			try {
				log.info("Response received in [" + currentResult.getLatency() + "ms] wait for [" + currentResult.getResponsivity() + "ms]");
				Thread.sleep(currentResult.getResponsivity());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			double memoryA = (currentExperiment.isMemoryLog()) ? Memory.getMemoryUsage() : 0D;
			this.currentResult.setMemoryA(memoryA);
			// this.currentResult.setCr(engineResult.getCompleteRHODF());
			// this.currentResult.setSr(engineResult.getSoundRHODF());
			// this.currentResult.setCs(engineResult.getCompleteSMPL());
			// this.currentResult.setSs(engineResult.getSoundSMPL());
			String filePath = outputPath + "/exp" + currentExperiment.getExperimentNumber() + "/" + engineName + "/" + experimentnName;

			boolean ret = collector.process(currentResult, filePath);
			this.tsResultEvents += ret ? 1 : 0;
			return ret;
		}

	}

	private boolean processAbox(RSPEngineResult engineResult) {

		this.aboxResult = new HeavenResult();
		this.aboxResult.setId("<http://example.org/" + currentExperiment.getExperimentNumber() + "/" + (engine.getEventNumber() - 1) + ">");
		this.aboxResult.setEventNumber(engineResult.getEventNumber());
		this.aboxResult.setInputTimestamp(engineResult.getInputTimestamp());
		this.aboxResult.setResult(engineResult);

		return collector.process(aboxResult, currentExperiment.getOutputPath());
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
			this.currentExperiment = e;
			this.outputPath = currentExperiment.getOutputPath() + currentExperiment.getDate();
			this.experimentnName = currentExperiment.getName();
			this.engineName = engine.getName();

			long startTime = System.currentTimeMillis();

			log.info("Status [" + status + "]" + " Start Running The Experiment [" + e.getExperimentNumber() + "] of date [" + e.getDate() + "]");

			status = ExecutionState.RUNNING;

			currentExperiment.setTimestampStart(startTime);

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

			this.currentExperiment.setTimestampEnd(System.currentTimeMillis());

			this.collector.process(currentExperiment, outputPath + "/database/");

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
