package it.polimi.processing.teststand.core;

import it.polimi.processing.EventProcessor;
import it.polimi.processing.Startable;
import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.Event;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.events.result.ExperimentResultEvent;
import it.polimi.processing.events.result.StreamingEventResult;
import it.polimi.processing.exceptions.WrongStatusTransitionException;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.streamer.Streamer;
import it.polimi.utils.FileUtils;
import it.polimi.utils.TripleGraphTypes;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

import org.apache.http.impl.cookie.DateUtils;

@Getter
@Log4j
public class TestStand<T extends RSPEngine> extends Stand implements EventProcessor<StreamingEvent>, ResultCollector<StreamingEventResult>,
		Startable<ExecutionStates> {

	private StartableCollector<StreamingEventResult> resultCollector;
	private StartableCollector<ExperimentResultEvent> experimentResultCollector;

	private T rspEngine;
	private Streamer<StreamingEvent> streamer;

	public TestStand() {
		super(ExecutionStates.NOT_READY, null);
	}

	public void build(StartableCollector<StreamingEventResult> resultCollector, StartableCollector<ExperimentResultEvent> experimentResultCollector,
			T rspEngine, Streamer<StreamingEvent> streamer) {
		this.experimentResultCollector = experimentResultCollector;
		this.resultCollector = resultCollector;
		this.rspEngine = rspEngine;
		this.streamer = streamer;
	}

	public int run(String f, int experimentNumber, String comment) throws Exception {
		log.info("START STREAMING " + System.currentTimeMillis());
		String experimentDescription = "EXPERIMENT_ON_" + f + "_WITH_ENGINE_" + rspEngine.getName();
		String inputFileName = FileUtils.INPUT_FILE_PATH + f;
		Date d = new Date();
		String outputFileName = "Result_" + DateUtils.formatDate(d, "YYYY_MM_dd_HH_mm_SS") + "_" + f.split("\\.")[0];

		if (!isOn()) {
			throw new WrongStatusTransitionException("Not ON");
		} else {

			log.debug("Status [" + status + "]" + " Start Running The Expeirment " + "Results will be named as [" + outputFileName + "]");

			status = ExecutionStates.RUNNING;

			currentExperiment = new Experiment(experimentNumber, experimentDescription, rspEngine.getName(), inputFileName, outputFileName,
					System.currentTimeMillis(), comment);

			log.debug("Status [" + status + "] Experiment Created");
			ExecutionStates engineStatus = rspEngine.startProcessing();
			log.debug("Status [" + status + "] Processing is started");

			if (ExecutionStates.READY.equals(engineStatus)) {
				try {
					streamer.stream(getBuffer(inputFileName), experimentNumber, rspEngine.getName(), TripleGraphTypes.UTRIPLES);
				} catch (IOException ex) {
					status = ExecutionStates.ERROR;
					log.error(ex.getMessage());
					return 0;
				}
			}

			engineStatus = rspEngine.stopProcessing();
			log.debug("Status [" + status + "] Processing is ended");

			experimentResultCollector.store(new ExperimentResultEvent(currentExperiment, System.currentTimeMillis()), "");

			if (ExecutionStates.CLOSED.equals(engineStatus)) {
				status = ExecutionStates.READY;
			}

			log.info("Status [" + status + "] Stop the Streamign " + System.currentTimeMillis());

		}
		return 1;
	}

	/**
	 * 
	 * Start the system execution Move the system state from ON to RUNNING
	 * 
	 * @param experimentNumber
	 * 
	 * @throws Exception
	 */
	public int run(String f, int experimentNumber) throws Exception {
		return run(f, experimentNumber, "");
	}

	@Override
	public ExecutionStates close() {
		if (!isOn()) {
			throw new WrongStatusTransitionException("Can't Switch from Status [" + status + "] to [" + ExecutionStates.CLOSED + "]");
		} else {
			ExecutionStates streamerStatus = streamer.close();
			ExecutionStates engineStatus = rspEngine.close();
			ExecutionStates collectorStatus = resultCollector.close();
			ExecutionStates experimenTcollectorStatus = experimentResultCollector.close();

			if (ExecutionStates.CLOSED.equals(streamerStatus) && ExecutionStates.CLOSED.equals(experimenTcollectorStatus)
					&& ExecutionStates.CLOSED.equals(collectorStatus) && ExecutionStates.CLOSED.equals(engineStatus)) {
				status = ExecutionStates.CLOSED;
				log.debug("Status [" + status + "] Closing the TestStand");
			} else {
				log.error("streamerStatus: " + streamerStatus);
				log.error("collectorStatus: " + collectorStatus);
				log.error("experimentCollectorStatus: " + experimenTcollectorStatus);
				log.error("engineStatus: " + engineStatus);
				status = ExecutionStates.ERROR;
			}
			return status;
		}
	}

	@Override
	public ExecutionStates init() {
		if (!isStartable()) {
			throw new WrongStatusTransitionException("Can't Switch from Status [" + status + "] to [" + ExecutionStates.READY + "]");
		} else {
			ExecutionStates streamerStatus = streamer.init();
			ExecutionStates engineStatus = rspEngine.init();
			ExecutionStates collectorStatus = resultCollector.init();
			ExecutionStates experimenTcollectorStatus = experimentResultCollector.init();
			if (ExecutionStates.READY.equals(streamerStatus) && ExecutionStates.READY.equals(collectorStatus)
					&& ExecutionStates.READY.equals(engineStatus) && ExecutionStates.READY.equals(experimenTcollectorStatus)) {
				status = ExecutionStates.READY;
				log.debug("Status [" + status + "] Initializing the TestStand");
			} else {
				log.error("streamerStatus [" + streamerStatus + "] collectorStatus [" + collectorStatus + "] experimentCollectorStatus ["
						+ experimenTcollectorStatus + "] engineStatus [" + engineStatus + "]");
				status = ExecutionStates.ERROR;
			}
			return status;
		}

	}

	public boolean isStartable() {
		return ExecutionStates.NOT_READY.equals(status) || ExecutionStates.CLOSED.equals(status);
	}

	public boolean isOn() {
		return ExecutionStates.READY.equals(status);
	}

	public boolean isReady() {
		return ExecutionStates.READY.equals(status);
	}

	/**
	 * Save the current execution state to log an error Stop the execution
	 * completely
	 * 
	 * @return
	 */
	public ExecutionStates stop() {
		return status = ExecutionStates.OFF;
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		return rspEngine.sendEvent(e);
	}

	@Override
	public boolean store(StreamingEventResult r, String where) {
		try {
			return resultCollector.store(r, rspEngine.getName() + "/" + currentExperiment.getOutputFileName());
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public long getTimestamp() {
		return resultCollector.getTimestamp();
	}

	@Override
	public StreamingEventResult newEventInstance(Set<String[]> all_triples, Event e) {
		return new StreamingEventResult((StreamingEvent) e, all_triples, System.currentTimeMillis());
	}

}
