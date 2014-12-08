package it.polimi.processing.workbench.core;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.Result;
import it.polimi.processing.events.TSResult;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.utils.FileUtils;
import it.polimi.utils.Memory;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Getter
@Setter
@Log4j
public class RSPTestStand extends TestStand {

	private int experimentNumber;
	private long timestamp, resultTimestamp = 0L;
	private double memoryA = 0D;
	private double memoryB = 0D;
	private String outputFileName, windowFileName, inputFileNameWithPath, experimentDescription;
	private final TimeStrategy timeStrategy;
	private TSResult currentResult;

	public RSPTestStand(TimeStrategy strategy) {
		super();
		this.timeStrategy = strategy;
	}

	@Override
	public int run(String f, int experimentNumber, String comment, String outputFileName, String windowFileName, String experimentDescription) {
		this.experimentNumber = experimentNumber;
		this.experimentDescription = experimentDescription;
		this.inputFileNameWithPath = FileUtils.INPUT_FILE_PATH + f;
		this.outputFileName = outputFileName;
		this.windowFileName = windowFileName;

		log.info("Start Streaming " + System.currentTimeMillis());

		if (!isOn()) {
			throw new WrongStatusTransitionException("Not ON");
		} else {
			log.info("Status [" + status + "]" + " Start Running The Experiment [" + experimentNumber + "] of date [" + FileUtils.d + "] "
					+ "Results will be named as [" + outputFileName + "]");

			status = ExecutionState.RUNNING;
			currentExperiment = new Experiment(experimentNumber, experimentDescription, rspEngine.getName(), inputFileNameWithPath, outputFileName,
					System.currentTimeMillis(), comment, 0L);
			log.debug("Status [" + status + "] Experiment Created");

			ExecutionState engineStatus = rspEngine.startProcessing();
			log.debug("Status [" + status + "] Processing is started");

			if (ExecutionState.READY.equals(engineStatus)) {
				try {
					rspEventStreamer.stream(getBuffer(inputFileNameWithPath), experimentNumber);
				} catch (IOException ex) {
					status = ExecutionState.ERROR;
					log.error(ex.getMessage());
					return 0;
				}
			}

			engineStatus = rspEngine.stopProcessing();

			log.debug("Status [" + status + "] Processing is ended");

			currentExperiment.setTimestampEnd(System.currentTimeMillis());
			experimentResultCollector.process(currentExperiment);

			if (ExecutionState.CLOSED.equals(engineStatus)) {
				status = ExecutionState.READY;
			} else if (ExecutionState.ERROR.equals(engineStatus)) {
				status = ExecutionState.ERROR;
			}

			log.info("Status [" + status + "] Stop the Streaming " + System.currentTimeMillis());

		}
		return 1;
	}

	@Override
	public boolean process(Event e) {
		return (e instanceof RSPEvent) ? process((RSPEvent) e) : process((Result) e);
	}

	public boolean process(RSPEvent e) {
		return timeStrategy.apply(e, this); // TODO bisogna rivedere il significato di strategy
	}

	@Override
	public boolean processDone() {
		log.debug("Process is Done, Window Shosts");
		resultTimestamp = System.currentTimeMillis();
		memoryA = Memory.getMemoryUsage();
		return rspEngine.processDone();
	}

	public boolean process(Result engineResult) {
		try {
			String w = "exp" + experimentNumber + "/" + rspEngine.getName() + "/" + ((engineResult.isAbox()) ? windowFileName : outputFileName);
			int eventNumber = rspEngine.getEventNumber();
			resultTimestamp = engineResult.getTimestamp();
			String id = "<http://example.org/" + experimentNumber + "/" + eventNumber + "/" + engineResult.getFrom() + "/" + engineResult.getTo()
					+ ">";
			TSResult r2 = new TSResult(id, eventNumber, engineResult.getStatements(), timestamp, resultTimestamp, memoryB, memoryA,
					engineResult.getCompleteSMPL(), engineResult.getSoundSMPL(), engineResult.getCompleteRHODF(), engineResult.getSoundRHODF());

			boolean ret = resultCollector.process(r2, w);

			return ret;
		} catch (IOException e) {
			return false;
		}
	}
}
