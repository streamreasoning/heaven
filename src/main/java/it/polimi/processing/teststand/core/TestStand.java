package it.polimi.processing.teststand.core;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionState;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.rspengine.esper.commons.listener.Result;
import it.polimi.processing.streamer.RSPEventStreamer;
import it.polimi.utils.FileUtils;
import it.polimi.utils.Memory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class TestStand extends Stand implements EventProcessor<RSPEvent>, ResultCollector<EventResult>, Startable<ExecutionState> {

	private StartableCollector<EventResult> resultCollector;
	private StartableCollector<ExperimentResult> experimentResultCollector;

	private RSPEngine rspEngine;
	private RSPEventStreamer RSPEventStreamer;
	private RSPEvent se;

	private int experimentNumber, numberEvents = 0;
	private long timestamp, resultTimestamp = 0L;
	private double memoryA = 0D;
	private double memoryB = 0D;
	private final DateFormat dt = new SimpleDateFormat("yyyy_MM_dd");
	private String outputFileName, windowFileName, inputFileNameWithPath, experimentDescription;

	public TestStand() {
		super(ExecutionState.NOT_READY, null);
	}

	public void build(StartableCollector<EventResult> resultCollector, StartableCollector<ExperimentResult> experimentResultCollector,
			RSPEngine rspEngine, RSPEventStreamer RSPEventStreamer) {
		this.experimentResultCollector = experimentResultCollector;
		this.resultCollector = resultCollector;
		this.rspEngine = rspEngine;
		this.RSPEventStreamer = RSPEventStreamer;
	}

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
	public ExecutionState close() {
		if (!isOn()) {
			throw new WrongStatusTransitionException("Can't Switch from Status [" + status + "] to [" + ExecutionState.CLOSED + "]");
		} else {
			ExecutionState RSPEventStreamerStatus = RSPEventStreamer.close();
			ExecutionState engineStatus = rspEngine.close();
			ExecutionState collectorStatus = resultCollector.close();
			ExecutionState experimenTcollectorStatus = experimentResultCollector.close();

			if (ExecutionState.CLOSED.equals(RSPEventStreamerStatus) && ExecutionState.CLOSED.equals(experimenTcollectorStatus)
					&& ExecutionState.CLOSED.equals(collectorStatus) && ExecutionState.CLOSED.equals(engineStatus)) {
				status = ExecutionState.CLOSED;
				log.debug("Status [" + status + "] Closing the TestStand");
			} else {
				log.error("RSPEventStreamerStatus: " + RSPEventStreamerStatus);
				log.error("collectorStatus: " + collectorStatus);
				log.error("experimentCollectorStatus: " + experimenTcollectorStatus);
				log.error("engineStatus: " + engineStatus);
				status = ExecutionState.ERROR;
			}
			return status;
		}
	}

	@Override
	public ExecutionState init() {
		if (!isStartable()) {
			throw new WrongStatusTransitionException("Can't Switch from Status [" + status + "] to [" + ExecutionState.READY + "]");
		} else {
			ExecutionState streamerStatus = RSPEventStreamer.init();
			ExecutionState engineStatus = rspEngine.init();
			ExecutionState collectorStatus = resultCollector.init();
			ExecutionState experimenTcollectorStatus = experimentResultCollector.init();
			if (ExecutionState.READY.equals(streamerStatus) && ExecutionState.READY.equals(collectorStatus)
					&& ExecutionState.READY.equals(engineStatus) && ExecutionState.READY.equals(experimenTcollectorStatus)) {
				status = ExecutionState.READY;
				log.debug("Status [" + status + "] Initializing the TestStand");
			} else {
				log.error("RSPEventStreamerStatus [" + streamerStatus + "] collectorStatus [" + collectorStatus + "] experimentCollectorStatus ["
						+ experimenTcollectorStatus + "] engineStatus [" + engineStatus + "]");
				status = ExecutionState.ERROR;
			}
			return status;
		}

	}

	public boolean isStartable() {
		return ExecutionState.NOT_READY.equals(status) || ExecutionState.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionState.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionState.READY.equals(status);
	}

	/**
	 * Save the current execution state to log an error Stop the execution
	 * completely
	 * 
	 * @return
	 */
	public ExecutionState stop() {
		return status = ExecutionState.OFF;
	}

	@Override
	public boolean process(RSPEvent e) {
		se = e;
		boolean process = false;
		if (numberEvents % 50 == 0 && numberEvents != 0) { // Stream 50 events at time
			memoryA = Memory.getMemoryUsage();
			process = processDone();
			memoryA = memoryB = 0D;
			timestamp = resultTimestamp = 0L;
		} else {
			if (numberEvents % 50 == 1 || numberEvents == 0) {
				memoryB = Memory.getMemoryUsage();
				timestamp = System.currentTimeMillis();
			}
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
			double memoryA2 = memoryA;
			double memoryB2 = memoryB;

			boolean ret = resultCollector.store(new TSResult(id, eventNumber, engineResult.getStatements(), timestamp, resultTimestamp, memoryA2,
					memoryB2));

			memoryA = memoryB = 0D;
			timestamp = resultTimestamp = 0L;

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

			return resultCollector
					.store(new TSResult(id, eventNumber, engineResult.getStatements(), timestamp, resultTimestamp, memoryA, memoryB), w);
		} catch (IOException e) {
			return false;
		}
	}
}
