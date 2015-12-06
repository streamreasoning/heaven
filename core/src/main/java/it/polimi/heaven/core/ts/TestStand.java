package it.polimi.heaven.core.ts;

import it.polimi.heaven.core.ts.collector.ResultCollector;
import it.polimi.heaven.core.ts.data.Experiment;
import it.polimi.heaven.core.ts.data.ExperimentExecution;
import it.polimi.heaven.core.ts.events.heaven.HeavenEvent;
import it.polimi.heaven.core.ts.rspengine.RSPEngine;
import it.polimi.heaven.core.ts.rspengine.Receiver;
import it.polimi.heaven.core.ts.streamer.Streamer;
import it.polimi.services.FileService;

import java.io.FileReader;

import lombok.extern.log4j.Log4j;

@Log4j
public class TestStand implements EventProcessor<HeavenEvent> {

	private Experiment current_experiment;
	private ExperimentExecution current_execution;

	private Streamer streamer;
	private RSPEngine engine;
	private Receiver receiver;
	private ResultCollector collector;

	public TestStand(Streamer streamer, RSPEngine engine, ResultCollector collector, Receiver receiver) {
		this.streamer = streamer;
		this.engine = engine;
		this.collector = collector;
		this.receiver = receiver;
	}

	public void init(Experiment e) {
		this.current_experiment = e;
		receiver.setNext(this);
		streamer.setCollector(this);
		engine.setNext(receiver);
		collector.setExperiment(e);
	}

	public ExperimentExecution run() {
		this.current_execution = new ExperimentExecution(current_experiment, System.currentTimeMillis());

		log.info("Start Running The Experiment [" + current_experiment.getExperimentNumber() + "] of date [" + current_experiment.getDate() + "]");

		log.debug("Experiment Created");

		engine.startProcessing();

		log.debug("Processing is started ");
		log.info("Loding file at [" + current_experiment.getInputSource() + "]");
		FileReader in = FileService.getFileReader(current_experiment.getInputSource());

		this.streamer.start(in);

		this.engine.stopProcessing();

		log.debug("Processing is ended ");

		this.current_execution.setTimestampEnd(System.currentTimeMillis());

		log.info("Stop the experiment, duration " + (current_execution.getTimestampEnd() - current_execution.getTimestampStart()) + "ms");
		return this.current_execution;
	}

	@Override
	public boolean process(HeavenEvent event) {
		return collector.process(event);
	}

	@Override
	public boolean setNext(EventProcessor<?> ep) {
		return false;
	}

}
