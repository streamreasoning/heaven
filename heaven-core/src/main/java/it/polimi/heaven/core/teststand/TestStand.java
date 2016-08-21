package it.polimi.heaven.core.teststand;

import it.polimi.heaven.core.teststand.collector.Receiver;
import it.polimi.heaven.core.teststand.collector.ResultCollector;
import it.polimi.data.Experiment;
import it.polimi.data.ExperimentExecution;
import it.polimi.streaming.Stimulus;
import it.polimi.heaven.core.teststand.events.HeavenEvent;
import it.polimi.heaven.core.teststand.streamer.Streamer;
import it.polimi.services.FileService;
import it.polimi.streaming.EventProcessor;
import lombok.extern.log4j.Log4j;

import java.io.FileReader;

@Log4j
public class TestStand implements EventProcessor<HeavenEvent> {

    private Experiment current_experiment;
    private ExperimentExecution current_execution;

    private Streamer streamer;
    private EventProcessor<Stimulus> engine;
    private Receiver receiver;
    private ResultCollector collector;

    public TestStand(Streamer streamer, EventProcessor<Stimulus> engine, ResultCollector collector, Receiver receiver) {
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

    @Override
    public void startProcessing() {
        run();
    }

    @Override
    public void stopProcessing() {

    }

}
