package it.polimi.processing.workbench.core;

import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.Result;
import it.polimi.processing.events.TSResult;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.utils.FileUtils;
import it.polimi.utils.Memory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class RSPTestStand extends TestStand<RSPEvent> {

	private int experimentNumber, numberEvents = 0;
	private long timestamp, resultTimestamp = 0L;
	private double memoryA = 0D;
	private double memoryB = 0D;
	private final DateFormat dt = new SimpleDateFormat("yyyy_MM_dd");
	private String outputFileName, windowFileName, inputFileNameWithPath, experimentDescription;

	@Override
	public int run(String f, int experimentNumber, String comment, String outputFileName, String windowFileName, String experimentDescription)
			throws Exception {
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
					RSPEventStreamer.stream(getBuffer(inputFileNameWithPath), experimentNumber);
				} catch (IOException ex) {
					status = ExecutionState.ERROR;
					log.error(ex.getMessage());
					return 0;
				}
			}

			engineStatus = rspEngine.stopProcessing();

			log.debug("Status [" + status + "] Processing is ended");

			currentExperiment.setTimestampEnd(System.currentTimeMillis());
			experimentResultCollector.store(currentExperiment);

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
	public boolean process(RSPEvent e) {
		se = e;
		boolean process = false;
		if (numberEvents == 0 || numberEvents % 50 == 1) {
			memoryB = Memory.getMemoryUsage();
			timestamp = System.currentTimeMillis();
		}

		if (numberEvents != 0 && numberEvents % 50 == 0) { // Stream 50 events at time
			memoryA = Memory.getMemoryUsage();
			process = processDone();
			memoryA = memoryB = 0D;
			timestamp = resultTimestamp = 0L;
		} else {
			process = rspEngine.process(e);
		}

		numberEvents++;
		return process;
	}

	@Override
	public boolean processDone() {
		log.info("Move window");
		resultTimestamp = System.currentTimeMillis();
		memoryA = Memory.getMemoryUsage();
		resultTimestamp = System.currentTimeMillis();
		return rspEngine.processDone();
	}

	@Override
	public boolean store(EventResult r) {
		try {
			Result engineResult = (Result) r;
			resultTimestamp = engineResult.getTimestamp();
			int eventNumber = rspEngine.getEventNumber();
			String id = "<http://example.org/" + experimentNumber + "/" + eventNumber + "/" + engineResult.getFrom() + "/" + engineResult.getTo()
					+ ">";
			Set<TripleContainer> statements = engineResult.getStatements();

			TSResult r2 = new TSResult(id, eventNumber, statements, timestamp, resultTimestamp, memoryA, memoryB, engineResult.getCompleteSMPL(),
					engineResult.getSoundSMPL(), engineResult.getCompleteRHODF(), engineResult.getSoundRHODF());
			boolean ret = resultCollector.store(r2);

			return ret;

		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean store(EventResult r, String where) throws IOException {

		try {
			Result engineResult = (Result) r;
			String w = rspEngine.getName() + "/" + (("Window".equals(where)) ? windowFileName : outputFileName);
			int eventNumber = rspEngine.getEventNumber();
			resultTimestamp = engineResult.getTimestamp();
			String id = "<http://example.org/" + experimentNumber + "/" + eventNumber + "/" + engineResult.getFrom() + "/" + engineResult.getTo()
					+ ">";

			TSResult r2 = new TSResult(id, eventNumber, engineResult.getStatements(), timestamp, resultTimestamp, memoryA, memoryB,
					engineResult.getCompleteSMPL(), engineResult.getSoundSMPL(), engineResult.getCompleteRHODF(), engineResult.getSoundRHODF());

			boolean ret = resultCollector.store(r2, w);
			return ret;
		} catch (IOException e) {
			return false;
		}
	}
}
